package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain

import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.AuthenticationState
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.SESSION_COOKIE_HEADER_NAME
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.SESSION_COOKIE_PREFS_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.TahomaAuthenticationApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.USER_TOKEN_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.models.ActivateTokenRequestBody
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import timber.log.Timber

class LoginUseCase(
    private val tahomaAuthenticationApi: TahomaAuthenticationApi,
    private val getUserCredentialsUseCase: GetUserCredentialsUseCase,
    private val encryptedSharedPreferences: EncryptedSharedPreferences
) : UseCase<Unit, String>() {

    override suspend fun doWork(params: Unit): String {
        Timber.d("Logging in...")

        val (username, password, pin) = getUserCredentialsUseCase(Unit).getOrThrow()

        val loginResponse = tahomaAuthenticationApi.login(username, password)
        val cookies: String = loginResponse
            .headers()
            .values("Set-Cookie")
            .first { it.contains(SESSION_COOKIE_HEADER_NAME) }
        val sessionCookie = "JSESSIONID=[^;]*;".toRegex().find(cookies)?.value?.replace(";", "")

        check(sessionCookie.orEmpty().isNotBlank()) { "Session cookie was empty!" }
        encryptedSharedPreferences.edit { putString(SESSION_COOKIE_PREFS_KEY, sessionCookie) }
        Timber.d("Logged in and stored session cookie: $sessionCookie")

        val userToken = tahomaAuthenticationApi.createToken(pin).token
        encryptedSharedPreferences.edit { putString(USER_TOKEN_KEY, userToken) }
        Timber.d("Created user token $userToken")

        tahomaAuthenticationApi.activateToken(
            pin = pin,
            body = ActivateTokenRequestBody(
                label = pin,
                token = userToken,
                scope = "devmode"
            )
        )

        AuthenticationState.loggedIn()

        Timber.d("Activated user token!")
        return userToken
    }
}