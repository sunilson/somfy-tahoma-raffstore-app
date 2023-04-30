package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.AuthenticationState
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.LoginUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber

class TahomaAuthenticator(
    private val context: Context,
    private val loginUseCase: LoginUseCase,
    private val encryptedSharedPreferences: EncryptedSharedPreferences
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.d("Starting authenticator for request ${response.request.url}")

        val previousAccessToken = response.request.header("Authorization")?.substringAfter("Bearer ")
        val previousRetryCount = response.request.header(KEY_RETRY_COUNT_HEADER)?.toInt() ?: 0

        if (previousAccessToken == null) {
            Timber.d("Stopping because previous request had no access token")
            AuthenticationState.loggedOut()
            return null
        }

        if (previousRetryCount > 2) {
            Timber.d("Stopping because retry count is over 2")
            AuthenticationState.loggedOut()
            return null
        }

        val refreshedAccessToken = synchronized(this) {
            // Check if in the meantime the access token has been refreshed
            val currentAccessToken = encryptedSharedPreferences.getString(USER_TOKEN_KEY, null).orEmpty()
            if (currentAccessToken.isNotBlank() && currentAccessToken != previousAccessToken) {
                Timber.d("Access token changed in the meantime, using new one")
                return@synchronized currentAccessToken
            }

            // Still no valid access token, now it is being refreshed via a network call
            Timber.d("Fetching new access token")
            return@synchronized runBlocking { loginUseCase(Unit).getOrNull() }
        }


        if (refreshedAccessToken == null) {
            Timber.d("Stopping because refreshed access token was null")
            AuthenticationState.loggedOut()
            return null
        }

        if (refreshedAccessToken == previousAccessToken) {
            Timber.d("Stopping because previous access token was the same as new one")
            AuthenticationState.loggedOut()
            return null
        }

        Timber.d("Restarting request with refreshed access token $refreshedAccessToken")
        return newRequestWithAccessToken(response.request, refreshedAccessToken, previousRetryCount + 1)
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String, retryCount: Int): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .header(KEY_RETRY_COUNT_HEADER, "$retryCount")
            .build()
    }
}