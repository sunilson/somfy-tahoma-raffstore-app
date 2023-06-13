package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiEvent
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.isActive
import timber.log.Timber

class ListenToEventsUseCase(
    private val tahomaLocalApi: TahomaLocalApi,
    private val updateDeviceStatesUseCase: UpdateDeviceStatesUseCase,
    private val loadAndSyncDevicesUseCase: LoadAndSyncDevicesUseCase,
    private val loadAndSyncDeviceUseCase: LoadAndSyncDeviceUseCase
) : FlowUseCase<Unit, Unit>() {

    private var listenerId: String? = null

    override fun doWork(params: Unit) = flow<Unit> {
        getListenerId()
        Timber.d("Registered listener $listenerId")

        while (currentCoroutineContext().isActive) {
            val newEvents = tahomaLocalApi.fetchNewEvents(listenerId!!)
            newEvents.forEach { handleEvent(it) }
            delay(1000)
        }

        awaitCancellation()
    }
        .catch { Timber.e(it) }
        .onCompletion { listenerId?.let { tahomaLocalApi.unRegisterEventListener(it) } }

    private suspend fun handleEvent(localApiEvent: LocalApiEvent) {
        Timber.d("Handling event: $localApiEvent")
        when (localApiEvent.type) {
            LocalApiEvent.Type.ExecutionStateChangedEvent -> handleExecutionStateChanged(localApiEvent)
            LocalApiEvent.Type.ExecutionRegisteredEvent -> {} // Do nothing. Executions are stored directly after sending request anyway
            LocalApiEvent.Type.DeviceProtocolAvailableEvent -> {} // Do nothing. Not important to us
            LocalApiEvent.Type.DeviceProtocolUnavailableEvent -> {} // Do nothing. Not important to us
            LocalApiEvent.Type.CommandExecutionStateChangedEvent -> {} // Do nothing. Not important to us
            LocalApiEvent.Type.DeviceStateChangedEvent -> handleDeviceStateChanged(localApiEvent)
            LocalApiEvent.Type.DeviceRemovedEvent -> loadAndSyncDevicesUseCase(Unit)
            LocalApiEvent.Type.DeviceCreatedEvent,
            LocalApiEvent.Type.DeviceUnavailableEvent,
            LocalApiEvent.Type.DeviceUpdatedEvent,
            LocalApiEvent.Type.DeviceAvailableEvent -> handleDeviceChanged(localApiEvent)
        }
    }

    private suspend fun handleDeviceChanged(localApiEvent: LocalApiEvent) {
        loadAndSyncDeviceUseCase(localApiEvent.deviceUrl ?: return)
    }

    private suspend fun handleDeviceStateChanged(localApiEvent: LocalApiEvent) {
        if (localApiEvent.deviceUrl != null && localApiEvent.deviceStates != null)
            updateDeviceStatesUseCase(UpdateDeviceStatesUseCase.Params(localApiEvent.deviceUrl, localApiEvent.deviceStates))
    }

    private suspend fun handleExecutionStateChanged(localApiEvent: LocalApiEvent) {
        if (localApiEvent.executionId != null && localApiEvent.finished) {
            // TODO Delete execution
        }

        if (localApiEvent.deviceUrl != null && localApiEvent.deviceStates != null) {
            updateDeviceStatesUseCase(UpdateDeviceStatesUseCase.Params(localApiEvent.deviceUrl, localApiEvent.deviceStates))
        }
    }

    private suspend fun getListenerId(): String {
        while (listenerId == null) {
            try {
                listenerId = tahomaLocalApi.registerEventListener().id
            } catch (error: Exception) {
                if (error is CancellationException) throw error
            }
            delay(1000)
        }

        return listenerId!!
    }
}