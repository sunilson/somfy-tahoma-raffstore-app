package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.ApiDevice
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import timber.log.Timber

class UpdateDeviceStatesUseCase(private val localDatabase: TahomaLocalDatabase) : UseCase<UpdateDeviceStatesUseCase.Params, Unit>() {

    data class Params(val deviceId: String, val updatedStates: List<ApiDevice.State>)

    override suspend fun doWork(params: Params) {
        Timber.d("Updating states of ${params.deviceId} to ${params.updatedStates}")
        val updatedDevice = localDatabase.dao().getDeviceOnce(params.deviceId)
            .let { updateSlateOrientation(it, params.updatedStates) }
            .let { updateClosurePercentage(it, params.updatedStates) }
            .let { updateIsMoving(it, params.updatedStates) }
        localDatabase.dao().updateDevice(updatedDevice)
    }

    private fun updateSlateOrientation(device: Device, states: List<ApiDevice.State>): Device {
        val newSlateOrientation = states
            .filterIsInstance<ApiDevice.State.IntState>()
            .firstOrNull { it.name == "core:SlateOrientationState" }
            ?.value
            ?: return device
        return device.copy(slateOrientation = newSlateOrientation)
    }

    private fun updateClosurePercentage(device: Device, states: List<ApiDevice.State>): Device {
        val newClosedPercentage = states
            .filterIsInstance<ApiDevice.State.IntState>()
            .firstOrNull { it.name == "core:ClosureState" }
            ?.value
            ?: return device
        return device.copy(closedPercentage = newClosedPercentage)
    }

    private fun updateIsMoving(device: Device, states: List<ApiDevice.State>): Device {
        val newIsMovingState = states
            .filterIsInstance<ApiDevice.State.BooleanState>()
            .firstOrNull { it.name == "core:MovingState" }
            ?.value
            ?: return device
        return device.copy(isMoving = newIsMovingState)
    }
}