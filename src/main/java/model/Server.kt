package model

import com.amazonaws.services.autoscaling.model.LifecycleState
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.InstanceStateName

data class Server(
        val instance: Instance,
        val asgInfo: com.amazonaws.services.autoscaling.model.Instance?
) {
    private fun tagValue(tagName: String): String? = instance.tags.find { it.key == tagName }?.value

    val stage: String = tagValue("Stage") ?: "Other"

    val app: String? = tagValue("App")

    val autoscalingLifecycleState: String? = asgInfo?.lifecycleState

    val instanceId: String = instance.instanceId

    val state = instance.state.name


    /* def goodorbad = (healthStatus, lifecycleState, state) match {
      case (_, "Pending", _) | (_, "Terminating", _) => "pending"
      case ("Healthy", "InService", Some("InService")) => "success"
      case ("Healthy", "InService", None) => "success"
      case _ => "danger"
    }
    */

    val cssClass = when {
        state != InstanceStateName.Running.toString() -> "grey lighten-2"
        autoscalingLifecycleState == LifecycleState.InService.toString() -> "green lighten-3"
        asgInfo == null -> "green lighten-3"
        else -> "red lighten-3"
    }
}