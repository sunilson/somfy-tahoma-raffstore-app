package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthenticationState {

    private val _loggedIn = MutableStateFlow(true)

    val loggedIn: StateFlow<Boolean> = _loggedIn.asStateFlow()

    fun loggedOut() {
        _loggedIn.value = false
    }

    fun loggedIn() {
        _loggedIn.value = true
    }

}