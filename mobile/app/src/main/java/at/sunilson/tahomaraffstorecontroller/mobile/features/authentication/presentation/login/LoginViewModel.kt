package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.R
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.CheckUserCredentialsExistingUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.LoginUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.StoreUserCredentialsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.entities.UserCredentials
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.TextFieldState
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val storeUserCredentialsUseCase: StoreUserCredentialsUseCase,
    private val loginUseCase: LoginUseCase,
    private val checkUserCredentialsExistingUseCase: CheckUserCredentialsExistingUseCase
) : BaseViewModel<LoginViewModel.State, LoginViewModel.SideEffect>(State()) {

    val username = savedStateHandle.getStateFlow(KEY_USERNAME, TextFieldState("weisslinus@gmail.com"))
    val password = savedStateHandle.getStateFlow(KEY_PASSWORD, TextFieldState("Linus-Weiss12"))
    val gatewayPin = savedStateHandle.getStateFlow(KEY_PIN, TextFieldState("2013-4725-5591"))

    fun loggedInCheckRequested() {
        viewModelScope.launch { checkUserCredentialsExistingUseCase(Unit).onSuccess { postSideEffect(SideEffect.LoggedIn) } }
    }

    fun loginButtonClicked() {
        Timber.d("Login button clicked")
        viewModelScope.launch {
            reduce { it.copy(loading = true) }
            storeUserCredentialsUseCase(UserCredentials(username.value.input, password.value.input, gatewayPin.value.input))
            loginUseCase(Unit).onSuccess { postSideEffect(SideEffect.LoggedIn) }
            reduce { it.copy(loading = false) }
        }
    }

    fun usernameChanged(username: String) {
        val error = if (username.isEmpty()) R.string.field_empty_error else null
        savedStateHandle[KEY_USERNAME] = TextFieldState(username, error)
    }

    fun passwordChanged(password: String) {
        val error = if (password.isEmpty()) R.string.field_empty_error else null
        savedStateHandle[KEY_PASSWORD] = TextFieldState(password, error)
    }

    fun gatewayPinChanged(pin: String) {
        val error = if (!pin.matches(validationRegex)) R.string.pin_invalid_error else null
        savedStateHandle[KEY_PIN] = TextFieldState(pin, error)
    }

    data class State(val loading: Boolean = false)

    sealed interface SideEffect {
        object LoggedIn : SideEffect
    }

    companion object {
        private val validationRegex = "^\\d{4}-\\d{4}-\\d{4}$".toRegex()
        private const val KEY_PASSWORD = "password"
        private const val KEY_USERNAME = "username"
        private const val KEY_PIN = "pin"
    }
}