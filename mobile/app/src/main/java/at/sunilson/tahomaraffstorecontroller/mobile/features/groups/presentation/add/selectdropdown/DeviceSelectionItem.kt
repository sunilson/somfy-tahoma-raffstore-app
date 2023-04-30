package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add.selectdropdown

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DeviceSelectionItem(device: Device, selected: Boolean, deviceClicked: (Device) -> Unit) {

    AnimatedContent(targetState = selected, label = "") { target ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { deviceClicked(device) }
                .padding(12.dp)
        ) {
            if (target) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Shows that this device is selected")
                Text(
                    device.name,
                    Modifier
                        .weight(1f)
                        .padding(start = 12.dp))
            } else {
                Text(device.name, Modifier.weight(1f))
            }
        }
    }
}