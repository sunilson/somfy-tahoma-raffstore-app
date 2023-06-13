package at.sunilson.tahomaraffstorecontroller.mobile.entities

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiDevice
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.entities.EntityWithId
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

@kotlinx.serialization.Serializable
@Immutable
@Stable
data class Device(
    override val id: String = "",
    val name: String = "",
    val available: Boolean = false,
    val synced: Boolean = false,
    @get:PropertyName("isClosed")
    val isClosed: Boolean = false,
    @get:PropertyName("isMoving")
    val isMoving: Boolean = false,
    val slateOrientation: Int = 0,
    val closedPercentage: Int = 0,
    val controllable: Boolean = false
): EntityWithId {
    constructor(localApiDevice: LocalApiDevice) : this(
        id = localApiDevice.deviceURL,
        name = localApiDevice.label,
        available = localApiDevice.available,
        synced = localApiDevice.synced,
        isClosed = localApiDevice.states
            .filterIsInstance<LocalApiDevice.State.StringState>()
            .firstOrNull { it.name == "core:OpenClosedState" }
            ?.value
            ?.equals("closed")
            ?: false,
        isMoving = localApiDevice.states
            .filterIsInstance<LocalApiDevice.State.BooleanState>()
            .firstOrNull { it.name == "core:MovingState" }
            ?.value ?: false,
        slateOrientation = localApiDevice.states
            .filterIsInstance<LocalApiDevice.State.IntState>()
            .firstOrNull { it.name == "core:SlateOrientationState" }
            ?.value
            ?: 0,
        closedPercentage = localApiDevice.states
            .filterIsInstance<LocalApiDevice.State.IntState>()
            .firstOrNull { it.name == "core:ClosureState" }
            ?.value ?: 0,
        controllable = localApiDevice.status == "available"
    )

    @get:Exclude
    val currentStateString: String
        get() = when {
            isMoving -> "Moving"
            isClosed -> "Bottom"
            closedPercentage != 100 -> "Partial"
            else -> "Top"
        }
}