package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Event(
    @SerialName("name")
    val type: Type,
    @SerialName("deviceURL")
    val deviceUrl: String? = null,
    @SerialName("execId")
    val executionId: String? = null,
    val oldState: ExecutionState? = null,
    val newState: ExecutionState? = null,
    val deviceStates: List<ApiDevice.State>? = null,
    val actions: List<ApiExecutionActionGroup.Action>? = null,
) {

    val finished: Boolean
        get() = newState == ExecutionState.COMPLETED || newState == ExecutionState.FAILED || newState == ExecutionState.CANCELLED

    enum class ExecutionState {
        INITIALIZED, IN_PROGRESS, COMPLETED, FAILED, CANCELLED, QUEUED_GATEWAY_SIDE, REJECTED
    }

    enum class Type {
        ExecutionStateChangedEvent,
        ExecutionRegisteredEvent,
        CommandExecutionStateChangedEvent,
        DeviceStateChangedEvent,
        DeviceCreatedEvent,
        DeviceRemovedEvent,
        DeviceUnavailableEvent,
        DeviceAvailableEvent,
        DeviceUpdatedEvent,
        DeviceProtocolAvailableEvent,
        DeviceProtocolUnavailableEvent;
    }
}
