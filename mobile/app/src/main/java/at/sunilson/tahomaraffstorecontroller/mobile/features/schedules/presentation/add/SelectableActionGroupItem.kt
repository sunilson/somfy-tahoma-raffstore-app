package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup

@Composable
fun SelectableActionGroupItem(
    actionGroup: ExecutionActionGroup,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (ExecutionActionGroup) -> Unit
) {

    AnimatedContent(targetState = selected, label = "") { target ->
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick(actionGroup) }
                .padding(12.dp)
        ) {
            if (target) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Shows that this device is selected")
                Text(actionGroup.label,
                    Modifier
                        .weight(1f)
                        .padding(start = 12.dp))
            } else {
                Text(actionGroup.label, Modifier.weight(1f))
            }
        }
    }
}