package at.sunilson.tahomaraffstorecontroller.mobile.features.discovery.domain

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import androidx.security.crypto.EncryptedSharedPreferences
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.GATEWAY_PIN_KEY
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.SaveTahomaBoxUrl
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.nio.charset.Charset

class DiscoverTahomaBoxUseCase(
    private val nsdManager: NsdManager,
    private val resolveUseCase: ResolveTahomaBoxInformationUseCase,
    private val saveTahomaBoxUrl: SaveTahomaBoxUrl,
    private val encryptedSharedPreferences: EncryptedSharedPreferences
) : FlowUseCase<Unit, DiscoverTahomaBoxUseCase.Result>() {

    sealed interface Result {
        object Searching : Result
        sealed class Box : Result {
            abstract val nsdServiceInfo: NsdServiceInfo

            data class Found(override val nsdServiceInfo: NsdServiceInfo) : Box()
            data class Lost(override val nsdServiceInfo: NsdServiceInfo) : Box()
        }

        object Error : Result
        object Stopped : Result
    }

    override fun doWork(params: Unit) = callbackFlow {
        Timber.d("Starting discovery...")

        val listener = object : NsdManager.DiscoveryListener {
            override fun onStartDiscoveryFailed(p0: String?, p1: Int) {
                trySend(Result.Error)
            }

            override fun onStopDiscoveryFailed(p0: String?, p1: Int) {
                trySend(Result.Error)
            }

            override fun onDiscoveryStarted(p0: String?) {
                trySend(Result.Searching)
            }

            override fun onDiscoveryStopped(p0: String?) {
                trySend(Result.Stopped)
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                trySend(Result.Box.Found(serviceInfo))
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                trySend(Result.Box.Lost(serviceInfo))
            }
        }

        nsdManager.discoverServices("_kizboxdev._tcp", NsdManager.PROTOCOL_DNS_SD, listener)
        awaitClose {
            nsdManager.stopServiceDiscovery(listener)
            Timber.d("Stopped discovery...")
        }
    }
        .mapNotNull {
            if (it is Result.Box) {
                if (it is Result.Box.Lost) return@mapNotNull it
                val resolvedInformation = resolveUseCase(it.nsdServiceInfo).getOrNull()
                val gatewayPin = resolvedInformation?.attributes?.get("gateway_pin")?.toString(
                    Charset.defaultCharset()
                )
                val expectedPin = encryptedSharedPreferences.getString(GATEWAY_PIN_KEY, null)
                if (gatewayPin != expectedPin) return@mapNotNull null
                saveTahomaBoxUrl(resolvedInformation!!)
                Result.Box.Found(resolvedInformation)
            } else {
                it
            }
        }
        .onEach { Timber.d("Got new discovery emission: $it") }
        .catch { Timber.e(it) }
}