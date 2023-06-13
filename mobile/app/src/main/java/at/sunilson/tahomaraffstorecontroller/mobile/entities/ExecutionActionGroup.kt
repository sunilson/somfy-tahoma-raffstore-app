package at.sunilson.tahomaraffstorecontroller.mobile.entities

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.entities.EntityWithId
import com.google.errorprone.annotations.Immutable
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
@Stable
data class ExecutionActionGroup(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    val label: String = "",
    val targetDeviceStates: List<Device> = emptyList()
) : EntityWithId