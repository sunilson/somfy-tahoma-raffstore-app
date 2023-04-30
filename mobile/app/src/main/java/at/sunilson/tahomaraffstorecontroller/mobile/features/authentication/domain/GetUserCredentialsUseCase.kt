package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain

import androidx.security.crypto.EncryptedSharedPreferences
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.GATEWAY_PIN_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.PASSWORD_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.USERNAME_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.entities.UserCredentials
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class GetUserCredentialsUseCase(private val encryptedSharedPreferences: EncryptedSharedPreferences) : UseCase<Unit, UserCredentials>() {
    override suspend fun doWork(params: Unit): UserCredentials {
        return UserCredentials(
            encryptedSharedPreferences.getString(USERNAME_KEY, null)!!,
            encryptedSharedPreferences.getString(PASSWORD_KEY, null)!!,
            encryptedSharedPreferences.getString(GATEWAY_PIN_KEY, null)!!
        )
    }
}