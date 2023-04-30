package at.sunilson.tahomaraffstorecontroller.mobile.features.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import at.sunilson.tahomaraffstorecontroller.mobile.features.deeplink.domain.HandleDeeplinksUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class FavouriteWidgetClickBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val handleDeeplinksUseCase: HandleDeeplinksUseCase by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("Received intent with data ${intent?.data}")
        GlobalScope.launch {
            handleDeeplinksUseCase(intent?.data ?: return@launch)
        }
    }
}