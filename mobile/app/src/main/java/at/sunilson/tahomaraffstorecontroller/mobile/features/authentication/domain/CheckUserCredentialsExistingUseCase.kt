package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain

import androidx.security.crypto.EncryptedSharedPreferences
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.GATEWAY_PIN_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.PASSWORD_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.USERNAME_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.USER_TOKEN_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class CheckUserCredentialsExistingUseCase(
    private val encryptedSharedPreferences: EncryptedSharedPreferences,
) : UseCase<Unit, Unit>() {

    override suspend fun doWork(params: Unit) {
        requireNotNull(encryptedSharedPreferences.getString(USER_TOKEN_KEY, null))
        requireNotNull(encryptedSharedPreferences.getString(USERNAME_KEY, null))
        requireNotNull(encryptedSharedPreferences.getString(PASSWORD_KEY, null))
        requireNotNull(encryptedSharedPreferences.getString(GATEWAY_PIN_KEY, null))
    }

}