package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.sunilson.tahomaraffstorecontroller.mobile.R
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.TahomaRaffstoreTheme
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.TextFieldState
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.components.LoadingOverlay

@Composable
fun LoginScreen(
    usernameState: TextFieldState = TextFieldState.empty(),
    passwordState: TextFieldState = TextFieldState.empty(),
    gatewayPinState: TextFieldState = TextFieldState.empty(),
    uiState: LoginViewModel.State = LoginViewModel.State(),
    loginButtonClicked: () -> Unit = {},
    usernameChanged: (String) -> Unit = {},
    passwordChanged: (String) -> Unit = {},
    gatewayPinChanged: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    LaunchedEffect(uiState.loading) { focusManager.clearFocus() }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 24.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = usernameState.input,
            onValueChange = usernameChanged,
            isError = usernameState.error != null,
            placeholder = { Text(stringResource(R.string.username_hint)) }
        )
        if (usernameState.error != null) {
            Text(
                text = stringResource(usernameState.error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordState.input,
            onValueChange = passwordChanged,
            isError = passwordState.error != null,
            placeholder = { Text(stringResource(R.string.password_hint)) },
            visualTransformation = PasswordVisualTransformation()
        )
        if (passwordState.error != null) {
            Text(
                text = stringResource(id = passwordState.error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = gatewayPinState.input,
            onValueChange = gatewayPinChanged,
            isError = gatewayPinState.error != null,
            placeholder = { Text(stringResource(R.string.pin_hint)) }
        )
        if (gatewayPinState.error != null) {
            Text(
                text = stringResource(id = gatewayPinState.error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = loginButtonClicked,
            enabled = !uiState.loading
                    && usernameState.error == null
                    && passwordState.error == null
                    && gatewayPinState.error == null
                    && usernameState.input.isNotEmpty()
                    && passwordState.input.isNotEmpty()
                    && gatewayPinState.input.isNotEmpty()
        ) { Text(text = "Login") }
    }

    AnimatedVisibility(visible = uiState.loading, enter = fadeIn(), exit = fadeOut()) {
        LoadingOverlay()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginDefaultPreview() {
    TahomaRaffstoreTheme {
        LoginScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginLoadingPreview() {
    TahomaRaffstoreTheme {
        LoginScreen(uiState = LoginViewModel.State(loading = true))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginErrorsPreview() {
    TahomaRaffstoreTheme {
        LoginScreen(
            uiState = LoginViewModel.State(loading = true),
            usernameState = TextFieldState("", R.string.field_empty_error),
            passwordState = TextFieldState("", R.string.field_empty_error),
        )
    }
}