package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device

@kotlinx.serialization.Serializable
@Entity
data class LocalExecutionActionGroup(
    @PrimaryKey val id: String,
    val label: String,
    val targetDeviceStates: List<Device>
)