package model

import com.amazonaws.services.ec2.model.InstanceState
import com.amazonaws.services.ec2.model.InstanceStateName
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.inject.ProvidedBy
import com.google.inject.Provider
import lib.AWS
import java.util.concurrent.TimeUnit
import com.amazonaws.services.autoscaling.model.Instance as AsgInstance
import com.amazonaws.services.ec2.model.Instance as Ec2Instance


class EstateQuery : Provider<Estate> {


    private val cache = CacheBuilder.newBuilder()
        .expireAfterWrite(30, TimeUnit.SECONDS)
        .build<String, Estate>(object : CacheLoader<String, Estate>() {
          override fun load(k: String) = uncachedGet()
        })


    override fun get(): Estate = cache.get("estate")

    fun uncachedGet(): Estate {
        println("building Estate...")
        val servers = buildServerMap()

        val asgs = AutoScalingServerGroup.loadAll(servers)

        val allServersSoFar = asgs.flatMap { it.servers }.map { it.instanceId }

        val otherServers = servers.values
                .filterNot { allServersSoFar.contains(it.instanceId) }
                .groupBy { it.app ?: "Other" }

        val otherServerGroups = otherServers.map {
            MiscServerGroup(name = it.key, servers = it.value)
        }

        val allGroups = (asgs + otherServerGroups).sortedBy { it.name }

        return Estate(allGroups, allGroups.flatMap { it.stages() }.distinct())
    }

    fun buildServerMap(): Map<String, Server> {
        // TODO: deal with "nextToken"
        return AWS.ec2
                .describeInstances()
                .reservations
                .flatMap { it.instances }
                .map { it.instanceId to Server(it, null) }
                .toMap()
    }

}

@ProvidedBy(EstateQuery::class)
data class Estate
(
    val groups: List<ServerGroup>,
    val allStages: List<String>,
    val stage: String? = null
)
{
    fun filterToStage(stage: String): Estate = this.copy(
            groups = groups.filter { it.stages().contains(stage) },
            stage = stage
    )

    fun isFilteredToStage(stage: String) = stage == this.stage

    fun findServer(instanceId: String): Server? =
        groups.flatMap { it.servers }.find { it.instanceId == instanceId }

}