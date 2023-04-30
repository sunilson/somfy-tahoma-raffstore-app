package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Event
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
    private val loadDevicesUseCase: LoadDevicesUseCase,
    private val loadDeviceUseCase: LoadDeviceUseCase,
    private val tahomaLocalDatabase: TahomaLocalDatabase
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

    private suspend fun handleEvent(event: Event) {
        Timber.d("Handling event: $event")
        when (event.type) {
            Event.Type.ExecutionStateChangedEvent -> handleExecutionStateChanged(event)
            Event.Type.ExecutionRegisteredEvent -> {} // Do nothing. Executions are stored directly after sending request anyway
            Event.Type.DeviceProtocolAvailableEvent -> {} // Do nothing. Not important to us
            Event.Type.DeviceProtocolUnavailableEvent -> {} // Do nothing. Not important to us
            Event.Type.CommandExecutionStateChangedEvent -> {} // Do nothing. Not important to us
            Event.Type.DeviceStateChangedEvent -> handleDeviceStateChanged(event)
            Event.Type.DeviceRemovedEvent -> loadDevicesUseCase(Unit)
            Event.Type.DeviceCreatedEvent,
            Event.Type.DeviceUnavailableEvent,
            Event.Type.DeviceUpdatedEvent,
            Event.Type.DeviceAvailableEvent -> handleDeviceChanged(event)
        }
    }

    private suspend fun handleDeviceChanged(event: Event) {
        loadDeviceUseCase(event.deviceUrl ?: return)
    }

    private suspend fun handleDeviceStateChanged(event: Event) {
        if (event.deviceUrl != null && event.deviceStates != null)
            updateDeviceStatesUseCase(UpdateDeviceStatesUseCase.Params(event.deviceUrl, event.deviceStates))
    }

    private suspend fun handleExecutionStateChanged(event: Event) {
        if (event.executionId != null && event.finished) {
            tahomaLocalDatabase.dao().deleteExecution(event.executionId)
        }

        if (event.deviceUrl != null && event.deviceStates != null) {
            updateDeviceStatesUseCase(UpdateDeviceStatesUseCase.Params(event.deviceUrl, event.deviceStates))
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