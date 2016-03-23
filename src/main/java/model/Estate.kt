package model

import com.google.inject.ProvidedBy
import com.google.inject.Provider
import lib.AWS
import com.amazonaws.services.ec2.model.Instance as Ec2Instance


data class Server(val instance: Ec2Instance) {
    private fun tagValue(tagName: String): String? = instance.tags.find { it.key == tagName }?.value

    val stage: String = tagValue("Stage") ?: "(unknown)"
    val app: String? = tagValue("App")
}


class EstateQuery : Provider<Estate> {
    override fun get(): Estate {
        val servers = buildServerMap()

        val asgs = AutoScalingServerGroup.loadAll(servers)

        // TODO: make this a fold when we introduce detection of EMR too
        val allServersSoFar = asgs.flatMap { it.servers }

        val otherServers = servers.filterValues { !allServersSoFar.contains(it) }

        val otherServerGroup = MiscServerGroup(otherServers.values)

        val allGroups = asgs + otherServerGroup

        return Estate(allGroups, allGroups.flatMap { it.stages() }.distinct())
    }

    fun buildServerMap(): Map<String, Server> {
        // TODO: deal with "nextToken"
        return AWS.ec2
                .describeInstances()
                .reservations
                .flatMap { it.instances }
                .map { it.instanceId to Server(it) }
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

}