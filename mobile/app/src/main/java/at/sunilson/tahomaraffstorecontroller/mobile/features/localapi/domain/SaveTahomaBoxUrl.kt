package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import android.net.nsd.NsdServiceInfo
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.GATEWAY_PIN_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.GATEWAY_URL_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import timber.log.Timber
import java.nio.charset.Charset

class SaveTahomaBoxUrl(
    private val encryptedSharedPreferences: EncryptedSharedPreferences
) : UseCase<NsdServiceInfo, Unit>() {
    override suspend fun doWork(params: NsdServiceInfo) {
        Timber.d("Starting connection to tahoma box...")

        val gatewayPinAttr = params.attributes["gateway_pin"]?.toString(Charset.defaultCharset())
        assert(
            gatewayPinAttr == encryptedSharedPreferences.getString(GATEWAY_PIN_KEY, null)
        ) { "Gateway pin did not match and was $gatewayPinAttr" }

        encryptedSharedPreferences.edit {
            putString(
                GATEWAY_URL_KEY,
                "https://${params.host.hostAddress}:${params.port}/enduser-mobile-web/1/enduserAPI/"
            )
        }
    }
}