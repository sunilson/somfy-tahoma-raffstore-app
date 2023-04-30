package at.sunilson.tahomaraffstorecontroller.mobile.main

import android.content.Intent
import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.AuthenticationState
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.CheckUserCredentialsExistingUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.deeplink.domain.HandleDeeplinksUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.discovery.domain.DiscoverTahomaBoxUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.ListenToEventsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.LoadDevicesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.RefreshExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.UpdateShortcuts
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel(
    private val listenToEventsUseCase: ListenToEventsUseCase,
    private val discoverTahomaBoxUseCase: DiscoverTahomaBoxUseCase,
    private val loadDevicesUseCase: LoadDevicesUseCase,
    private val refreshExecutions: RefreshExecutions,
    private val updateShortcuts: UpdateShortcuts,
    private val handleDeeplinksUseCase: HandleDeeplinksUseCase,
    private val checkUserCredentialsExistingUseCase: CheckUserCredentialsExistingUseCase
) : BaseViewModel<MainViewModel.State, MainViewModel.SideEffect>(State) {
    object State
    sealed interface SideEffect

    private var pollJob: Job? = null
    private var discoverJob: Job? = null
    private var boxDiscovered = false

    fun onViewResumed() {
        Timber.d("Resumed")
        discoverBox()
        pollEvents()
        pollDevicesAndExecutions()
        viewModelScope.launch { checkUserCredentialsExistingUseCase(Unit).onFailure { AuthenticationState.loggedOut() } }
        viewModelScope.launch { updateShortcuts(Unit) }
    }

    fun onViewPaused() {
        Timber.d("Paused")
        pollJob?.cancel()
        discoverJob?.cancel()
    }

    fun onNewIntent(intent: Intent) = viewModelScope.launch {
        val data = intent.data ?: return@launch
        handleDeeplinksUseCase(data)
    }

    private fun discoverBox() {
        discoverJob?.cancel()
        discoverJob = viewModelScope.launch {
            discoverTahomaBoxUseCase(Unit)
                .onCompletion { boxDiscovered = false }
                .collect { result ->
                    Timber.tag(LOG_TAG).d("New box emission $result")
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
        viewModelScope.launch {
            loadDevicesUseCase(Unit).fold(
                { Timber.d("Loaded new devices!") },
                { Timber.e(it) }
            )
        }
    }

    private fun refreshExecutions() {
        viewModelScope.launch {
            refreshExecutions(Unit).fold(
                { Timber.d("Loaded new executions!") },
                { Timber.e(it) }
            )
        }
    }

    private fun pollDevicesAndExecutions() {
        viewModelScope.launch {
            delay(5000)
            loadDevices()
            refreshExecutions()
        }
    }

    private fun pollEvents() {
        pollJob?.cancel()
        pollJob = viewModelScope.launch { listenToEventsUseCase(Unit).collect {} }
    }

    companion object {
        private const val LOG_TAG = "MainViewModel"
    }
}