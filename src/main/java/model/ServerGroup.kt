package model

import com.amazonaws.services.autoscaling.model.AutoScalingGroup
import lib.AWS

abstract class ServerGroup
{
    abstract val name: String
    abstract val servers: Collection<Server>

    fun stages() = servers.map { it.stage }.toSet()
}

class AutoScalingServerGroup(val asg: AutoScalingGroup, override val servers: Collection<Server>) : ServerGroup() {
    private fun tagValue(tagName: String): String? = asg.tags.find { it.key == tagName }?.value

    val stage: String = tagValue("Stage") ?: "Other"
    val app: String = tagValue("App") ?: asg.autoScalingGroupName


    override val name: String = app

    companion object {
        fun loadAll(serverMap: Map<String, Server>): List<AutoScalingServerGroup> {

            // TODO: deal with nextToken here
            val autoScalingGroups = AWS.autoscaling.describeAutoScalingGroups().autoScalingGroups

            return autoScalingGroups.map { autoScalingGroup ->
                val s = autoScalingGroup.instances.map { serverMap[it.instanceId] }.requireNoNulls()
                AutoScalingServerGroup(asg = autoScalingGroup, servers = s)
            }
        }
    }
}

class MiscServerGroup(override val servers: Collection<Server>) : ServerGroup() {
    override val name: String = "(other servers)"
}
