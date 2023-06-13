package at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.time.LocalTime

@Composable
fun ConfirmTextDialog(
    title: String = "",
    hint: String = "",
    text: String = "",
    onTextChanged: (String) -> Unit = {},
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            AutoFocusTextField(
                value = text,
                onValueChange = onTextChanged,
                placeholder = { Text(hint) })
        },
        confirmButton = { Button(onClick = onConfirm) { Text("Save") } },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )

}