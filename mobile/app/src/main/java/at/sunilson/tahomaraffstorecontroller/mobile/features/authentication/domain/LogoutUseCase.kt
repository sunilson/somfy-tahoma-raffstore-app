package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain

import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.GATEWAY_PIN_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.PASSWORD_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.USERNAME_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.USER_TOKEN_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class LogoutUseCase(private val encryptedSharedPreferences: EncryptedSharedPreferences) : UseCase<Unit, Unit>() {
    override suspend fun doWork(params: Unit) {
        encryptedSharedPreferences.edit {
            remove(USERNAME_KEY)
            remove(PASSWORD_KEY)
            remove(GATEWAY_PIN_KEY)
            remove(USER_TOKEN_KEY)
        }
    }

}