package at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoFocusTextField(value: String, placeholder: @Composable () -> Unit, onValueChange: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "Clear textfield",
                    modifier = Modifier.clickable { onValueChange("") }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}