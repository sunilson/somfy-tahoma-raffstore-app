package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data

import androidx.security.crypto.EncryptedSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class SessionCookieInterceptor(private val encryptedSharedPreferences: EncryptedSharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val cookie = encryptedSharedPreferences.getString(SESSION_COOKIE_PREFS_KEY, null).orEmpty()
        Timber.d("Adding session cookie to request: $cookie")
        builder.addHeader("Cookie", cookie)
        return chain.proceed(builder.build())
    }
}