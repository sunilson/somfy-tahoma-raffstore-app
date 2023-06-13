package at.sunilson.tahomaraffstorecontroller.mobile.entities

import androidx.compose.runtime.Immutable
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.entities.EntityWithId

@kotlinx.serialization.Serializable
@Immutable
data class Execution(
    override val id: String = "",
    val actionGroupLabel: String = "",
    val deviceIds: List<String> = emptyList()
) : EntityWithId