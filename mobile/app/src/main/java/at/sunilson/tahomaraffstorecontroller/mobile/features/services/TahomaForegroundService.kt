package at.sunilson.tahomaraffstorecontroller.mobile.features.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.features.discovery.domain.DiscoverTahomaBoxUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ExecuteLocalApiAction
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ListenToEventsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.LoadAndSyncDevicesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ObserveRemoteActionsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.RefreshExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.main.MainViewModel
import at.sunilson.tahomaraffstorecontroller.mobile.main.ServicesCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class TahomaForegroundService : Service(), CoroutineScope by CoroutineScope(Dispatchers.Main + SupervisorJob()) {

    private val discoverTahomaBoxUseCase: DiscoverTahomaBoxUseCase by inject()
    private val loadAndSyncDevicesUseCase: LoadAndSyncDevicesUseCase by inject()
    private val refreshExecutions: RefreshExecutions by inject()
    private val observeRemoteActionsUseCase: ObserveRemoteActionsUseCase by inject()
    private val executeLocalApiAction: ExecuteLocalApiAction by inject()
    private val listenToEventsUseCase: ListenToEventsUseCase by inject()

    private var pollJob: Job? = null
    private var actionsJob: Job? = null
    private var discoverJob: Job? = null
    private var boxDiscovered = false
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_NOT_STICKY

    override fun onCreate() {
        super.onCreate()
        Timber.d("Created TahomaForegroundService")
        startForeground(
            NOTIFICATION_ID,
            NotificationsUtil.createNotification(
                context = applicationContext,
                title = "Tahoma Raffstore Controller",
                content = "Observing local api executions",
                channelId = CHANNEL_ID,
                channelName = "LocalApi"
            )
        )

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tahoma::TahomaWakeLock").apply { acquire() }
        }

        discoverBox()
        pollDevicesAndExecutions()
        pollEvents()
        pollActionsToExecute()

        ServicesCache.isTahomaServiceRunning.value = true
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        wakeLock.release()
        ServicesCache.isTahomaServiceRunning.value = false
    }

    private fun pollDevicesAndExecutions() {
        launch {
            delay(5000)
            loadDevices()
            refreshExecutions()
        }
    }

    private fun refreshExecutions() {
        launch {
            refreshExecutions(Unit).fold(
                { Timber.d("Loaded new executions!") },
                { Timber.e(it) }
            )
        }
    }

    private fun discoverBox() {
        discoverJob?.cancel()
        discoverJob = launch {
            discoverTahomaBoxUseCase(Unit)
                .onCompletion { boxDiscovered = false }
                .collect { result ->
                    Timber.d("New box emission $result")
                    if (result is DiscoverTahomaBoxUseCase.Result.Box.Found) {
                        boxDiscovered = true
                        loadDevices()
                        refreshExecutions()
                    } else {
                        boxDiscovered = false
                    }
                }
        }
    }

    private fun loadDevices() {
        launch {
            loadAndSyncDevicesUseCase(Unit).fold(
                { Timber.d("Loaded new devices!") },
                { Timber.e(it) }
            )
        }
    }

    private fun pollActionsToExecute() {
        actionsJob?.cancel()
        actionsJob = launch {
            observeRemoteActionsUseCase(Unit).collect { (key, actionToExecute) ->
                executeLocalApiAction(ExecuteLocalApiAction.Params(remoteChildId = key, actionToExecute = actionToExecute))
            }
        }
    }

    private fun pollEvents() {
        pollJob?.cancel()
        pollJob = launch { listenToEventsUseCase(Unit).collect {} }
    }

    companion object {
        private const val CHANNEL_ID = "tahomaLocalApi2"
        private const val NOTIFICATION_ID = 5678
    }
}