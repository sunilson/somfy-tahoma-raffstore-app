package at.sunilson.tahomaraffstorecontroller.mobile.features.discovery.domain

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ResolveTahomaBoxInformationUseCase(private val nsdManager: NsdManager) :
    UseCase<NsdServiceInfo, NsdServiceInfo>() {

    override suspend fun doWork(params: NsdServiceInfo) = withTimeout(60000) {
        suspendCancellableCoroutine { cont ->
            val listener = object : NsdManager.ResolveListener {
                override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                    cont.resumeWithException(IllegalStateException("Failed to connect to service with error code: $errorCode"))
                }

                override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                    cont.resume(serviceInfo)
                }
            }
            nsdManager.resolveService(params, listener)
        }
    }
}