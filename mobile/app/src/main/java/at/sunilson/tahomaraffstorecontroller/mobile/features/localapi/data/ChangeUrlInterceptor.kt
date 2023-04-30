package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data

import androidx.security.crypto.EncryptedSharedPreferences
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.GATEWAY_URL_KEY
import okhttp3.Interceptor
import okhttp3.Response

class ChangeUrlInterceptor(private val encryptedSharedPreferences: EncryptedSharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val realUrl = encryptedSharedPreferences.getString(GATEWAY_URL_KEY, "").orEmpty()

        if (realUrl.isBlank()) return chain.proceed(request)

        val newRequest = request
            .newBuilder()
            .url(request.url.toString().replace("https://replace.me/", realUrl))
            .build()

        return chain.proceed(newRequest)
    }
}