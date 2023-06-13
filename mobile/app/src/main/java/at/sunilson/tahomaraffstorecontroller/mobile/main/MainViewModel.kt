package at.sunilson.tahomaraffstorecontroller.mobile.main

import android.content.Intent
import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.AuthenticationState
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.CheckUserCredentialsExistingUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.deeplink.domain.HandleDeeplinksUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.UpdateShortcuts
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel(
    private val updateShortcuts: UpdateShortcuts,
    private val handleDeeplinksUseCase: HandleDeeplinksUseCase,
    private val checkUserCredentialsExistingUseCase: CheckUserCredentialsExistingUseCase,
) : BaseViewModel<MainViewModel.State, MainViewModel.SideEffect>(State) {
    object State
    sealed interface SideEffect


    fun onViewResumed() {
        Timber.d("Resumed")
        viewModelScope.launch { checkUserCredentialsExistingUseCase(Unit).onFailure { AuthenticationState.loggedOut() } }
        viewModelScope.launch { updateShortcuts(Unit) }
    }

    fun onViewPaused() {
        Timber.d("Paused")
    }

    fun onNewIntent(intent: Intent) = viewModelScope.launch {
        val data = intent.data ?: return@launch
        handleDeeplinksUseCase(data)
    }
}