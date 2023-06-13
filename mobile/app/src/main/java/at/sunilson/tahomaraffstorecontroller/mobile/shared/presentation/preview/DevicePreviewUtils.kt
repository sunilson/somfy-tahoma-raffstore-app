package at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.preview

import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiDevice
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import java.util.UUID

object DevicePreviewUtils {

    fun default(label: String = UUID.randomUUID().toString()) = Device(
        LocalApiDevice(
            deviceURL = UUID.randomUUID().toString(),
            label = label,
            available = true,
            synced = true,
            type = 0,
            states = listOf(LocalApiDevice.State.StringState(0, "core:StatusState", "available"))
        )
    )

    fun unavailable(label: String = UUID.randomUUID().toString()) = default(label).copy(available = false)

    fun moving(label: String = UUID.randomUUID().toString()) = default(label).copy(isMoving = true)

}