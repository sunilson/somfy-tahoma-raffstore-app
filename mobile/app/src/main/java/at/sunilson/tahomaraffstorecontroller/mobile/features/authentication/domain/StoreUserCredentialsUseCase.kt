package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain

import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.GATEWAY_PIN_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.PASSWORD_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.USERNAME_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.entities.UserCredentials
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class StoreUserCredentialsUseCase(private val encryptedSharedPreferences: EncryptedSharedPreferences) : UseCase<UserCredentials, Unit>() {
    override suspend fun doWork(params: UserCredentials) {
        encryptedSharedPreferences.edit {
            putString(USERNAME_KEY, params.userName)
            putString(PASSWORD_KEY, params.password)
            putString(GATEWAY_PIN_KEY, params.pin)
        }
    }
}