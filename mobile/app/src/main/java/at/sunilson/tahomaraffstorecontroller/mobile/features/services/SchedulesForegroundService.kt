package at.sunilson.tahomaraffstorecontroller.mobile.features.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.ExecuteScheduleUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.ObserveSchedulesToExecuteUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.main.ServicesCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class SchedulesForegroundService : Service(), CoroutineScope by CoroutineScope(Dispatchers.Main + SupervisorJob()) {

    private val observeSchedulesToExecuteUseCase: ObserveSchedulesToExecuteUseCase by inject()
    private val executeScheduleUseCase: ExecuteScheduleUseCase by inject()
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_NOT_STICKY

    override fun onCreate() {
        super.onCreate()
        Timber.d("Created SchedulesForegroundService")
        startForeground(
            NOTIFICATION_ID,
            NotificationsUtil.createNotification(
                context = applicationContext,
                title = "Tahoma Raffstore Controller",
                content = "Observing schedules and executing them when needed",
                channelId = CHANNEL_ID,
                channelName = "Schedules"
            )
        )

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tahoma::SchedulesWakeLock").apply { acquire() }
        }

        observeSchedulesToExecute()

        ServicesCache.isSchedulesServiceRunning.value = true
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        wakeLock.release()
        ServicesCache.isSchedulesServiceRunning.value = false
    }

    private fun observeSchedulesToExecute() {
        launch {
            observeSchedulesToExecuteUseCase(Unit).collect { schedules ->
                schedules.forEach { launch { executeScheduleUseCase(it) } }
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "tahomaSchedules2"
        private const val NOTIFICATION_ID = 1234
    }
}