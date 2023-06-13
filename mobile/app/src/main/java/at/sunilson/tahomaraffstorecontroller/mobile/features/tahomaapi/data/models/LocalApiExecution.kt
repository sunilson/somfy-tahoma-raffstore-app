package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models

import androidx.compose.runtime.Immutable

@kotlinx.serialization.Serializable
@Immutable
data class LocalApiExecution(
    val id: String,
    val owner: String,
    val actionGroup: LocalApiExecutionActionGroup
)
