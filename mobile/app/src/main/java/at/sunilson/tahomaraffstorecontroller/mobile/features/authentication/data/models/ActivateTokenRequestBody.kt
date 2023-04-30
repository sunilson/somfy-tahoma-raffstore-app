package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.models

@kotlinx.serialization.Serializable
data class ActivateTokenRequestBody(
    val label: String,
    val token: String,
    val scope: String
)