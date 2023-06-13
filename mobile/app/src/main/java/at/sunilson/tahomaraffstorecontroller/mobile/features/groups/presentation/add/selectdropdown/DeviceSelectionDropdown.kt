package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add.selectdropdown

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DeviceSelectionDropdown(devices: ImmutableList<Device>, selectedDevices: ImmutableList<String>, deviceClicked: (Device) -> Unit) {
    var dropdownExpanded by rememberSaveable { mutableStateOf(true) }
    val dropdownIconRotation: Float by animateFloatAsState(
        targetValue = if (dropdownExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing), label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { dropdownExpanded = !dropdownExpanded }
            .padding(12.dp)
    ) {
        Text(text = "${selectedDevices.size}/${devices.size} Raffstores selektiert", modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Expand raffstore list",
            modifier = Modifier.rotate(dropdownIconRotation)
        )
    }
    AnimatedVisibility(
        visible = dropdownExpanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column {
            devices.forEach { device ->
                DeviceSelectionItem(
                    device = device,
                    selected = selectedDevices.contains(device.id),
                    deviceClicked = deviceClicked
                )
            }
        }
    }
}