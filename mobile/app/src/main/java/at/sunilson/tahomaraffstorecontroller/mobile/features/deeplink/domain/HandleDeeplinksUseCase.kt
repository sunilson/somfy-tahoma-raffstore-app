package at.sunilson.tahomaraffstorecontroller.mobile.features.deeplink.domain

import android.net.Uri
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.QUERY_EXECUTE_ACTION_GROUP_ID
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.QUERY_EXECUTE_DEVICE_URL
import at.sunilson.tahomaraffstorecontroller.mobile.features.discovery.domain.DiscoverTahomaBoxUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.ExecuteAction
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.ExecuteActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.DeviceAction
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

class HandleDeeplinksUseCase(
    private val discoverTahomaBoxUseCase: DiscoverTahomaBoxUseCase,
    private val executeActionGroup: ExecuteActionGroup,
    private val executeAction: ExecuteAction
) : UseCase<Uri, Unit>() {

    override suspend fun doWork(params: Uri) {
        discoverBox()

        if (params.path?.contains("action/execute/group") == true) {
            executeActionGroup(params.getQueryParameter(QUERY_EXECUTE_ACTION_GROUP_ID).orEmpty())
        }

        if (params.path?.contains("action/execute/device/my") == true) {
            executeAction(DeviceAction.MyPosition(params.getQueryParameter(QUERY_EXECUTE_DEVICE_URL)!!))
        }
    }

    private suspend fun discoverBox() {
        coroutineScope {
            withTimeout(TimeUnit.SECONDS.toMillis(10)) {
                discoverTahomaBoxUseCase(Unit).filter { it is DiscoverTahomaBoxUseCase.Result.Box.Found }.first()
            }
        }
    }

}