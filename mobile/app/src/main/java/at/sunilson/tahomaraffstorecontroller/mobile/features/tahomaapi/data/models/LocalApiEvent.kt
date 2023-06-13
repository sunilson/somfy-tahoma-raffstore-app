package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LocalApiEvent(
    @SerialName("name")
    val type: Type,
    @SerialName("deviceURL")
    val deviceUrl: String? = null,
    @SerialName("execId")
    val executionId: String? = null,
    val oldState: ExecutionState? = null,
    val newState: ExecutionState? = null,
    val deviceStates: List<LocalApiDevice.State>? = null,
    val actions: List<LocalApiExecutionActionGroup.Action>? = null,
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
