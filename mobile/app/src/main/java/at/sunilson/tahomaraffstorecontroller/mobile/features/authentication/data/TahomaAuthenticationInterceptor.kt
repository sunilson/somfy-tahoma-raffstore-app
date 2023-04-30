package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data

import androidx.security.crypto.EncryptedSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class TahomaAuthenticationInterceptor(private val encryptedSharedPreferences: EncryptedSharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = encryptedSharedPreferences.getString(USER_TOKEN_KEY, null).orEmpty()
        return chain.proceed(
            chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
        )
    }
}