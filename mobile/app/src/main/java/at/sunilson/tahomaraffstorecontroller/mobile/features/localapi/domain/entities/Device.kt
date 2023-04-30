package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.ApiDevice

@kotlinx.serialization.Serializable
@Entity(tableName = "Device")
@Immutable
data class Device(
    @PrimaryKey
    val id: String,
    val name: String,
    val available: Boolean,
    val synced: Boolean,
    val isClosed: Boolean,
    val isMoving: Boolean,
    val slateOrientation: Int,
    val closedPercentage: Int,
    val controllable: Boolean
) {
    constructor(apiDevice: ApiDevice) : this(
        id = apiDevice.deviceURL,
        name = apiDevice.label,
        available = apiDevice.available,
        synced = apiDevice.synced,
        isClosed = apiDevice.states
            .filterIsInstance<ApiDevice.State.StringState>()
            .firstOrNull { it.name == "core:OpenClosedState" }
            ?.value
            ?.equals("closed")
            ?: false,
        isMoving = apiDevice.states
            .filterIsInstance<ApiDevice.State.BooleanState>()
            .firstOrNull { it.name == "core:MovingState" }
            ?.value ?: false,
        slateOrientation = apiDevice.states
            .filterIsInstance<ApiDevice.State.IntState>()
            .firstOrNull { it.name == "core:SlateOrientationState" }
            ?.value
            ?: 0,
        closedPercentage = apiDevice.states
            .filterIsInstance<ApiDevice.State.IntState>()
            .firstOrNull { it.name == "core:ClosureState" }
            ?.value ?: 0,
        controllable = apiDevice.status == "available"
    )

    val currentStateString: String
        get() = when {
            isMoving -> "Moving"
            isClosed -> "Bottom"
            closedPercentage != 100 -> "Partial"
            else -> "Top"
        }
}