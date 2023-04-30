package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.serialization.Serializable
@Entity
@Stable
@Immutable
data class Execution(@PrimaryKey val id: String, val owner: String, val actionGroup: ApiExecutionActionGroup)
