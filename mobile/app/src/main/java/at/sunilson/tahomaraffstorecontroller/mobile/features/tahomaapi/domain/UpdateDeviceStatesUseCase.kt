package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiDevice
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.sanitizeFirebaseId
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class UpdateDeviceStatesUseCase(private val firebaseDatabase: FirebaseDatabase) : UseCase<UpdateDeviceStatesUseCase.Params, Unit>() {

    data class Params(val deviceId: String, val updatedStates: List<LocalApiDevice.State>)

    override suspend fun doWork(params: Params) {
        Timber.d("Updating states of ${params.deviceId} to ${params.updatedStates}")

        val newSlateOrientation = params.updatedStates
            .filterIsInstance<LocalApiDevice.State.IntState>()
            .firstOrNull { it.name == "core:SlateOrientationState" }
            ?.value

        val newClosedPercentage = params.updatedStates
            .filterIsInstance<LocalApiDevice.State.IntState>()
            .firstOrNull { it.name == "core:ClosureState" }
            ?.value

        val newIsMovingState = params.updatedStates
            .filterIsInstance<LocalApiDevice.State.BooleanState>()
            .firstOrNull { it.name == "core:MovingState" }
            ?.value

        firebaseDatabase.reference
            .child("devices")
            .child(params.deviceId.sanitizeFirebaseId())
            .updateChildren(
                mapOf(
                    "slateOrientation" to newSlateOrientation,
                    "closedPercentage" to newClosedPercentage,
                    "isMoving" to newIsMovingState,
                ).filter { it.value != null }
            )
    }
}