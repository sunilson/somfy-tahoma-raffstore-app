package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestBody(val userId: String, val userPassword: String)