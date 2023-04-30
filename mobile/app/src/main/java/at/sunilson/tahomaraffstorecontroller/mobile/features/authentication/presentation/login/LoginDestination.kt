package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.presentation.login

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.presentation.login.LoginViewModel.SideEffect.LoggedIn
import at.sunilson.tahomaraffstorecontroller.mobile.main.MainActivity
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginDestination() {
    val viewModel = koinViewModel<LoginViewModel>()

    val username by viewModel.username.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val gatewayPin by viewModel.gatewayPin.collectAsStateWithLifecycle()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            LoggedIn -> context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    LaunchedEffect(true) { viewModel.loggedInCheckRequested() }

    LoginScreen(
        usernameState = username,
        passwordState = password,
        gatewayPinState = gatewayPin,
        uiState = uiState,
        loginButtonClicked = viewModel::loginButtonClicked,
        usernameChanged = viewModel::usernameChanged,
        passwordChanged = viewModel::passwordChanged,
        gatewayPinChanged = viewModel::gatewayPinChanged,
    )
}