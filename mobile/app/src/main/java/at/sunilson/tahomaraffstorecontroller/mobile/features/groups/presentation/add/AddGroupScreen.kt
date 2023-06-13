package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add.selectdropdown.DeviceSelectionDropdown
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ActionToExecute
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.presentation.overview.RaffstoreListItem
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.components.AutoFocusTextField
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.components.ConfirmTextDialog
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddGroupScreen(
    devices: ImmutableList<Device>,
    selectedDevices: ImmutableList<String>,
    showSaveDialog: Boolean,
    saveDialogInputValue: String,
    onSaveDialogInputChanged: (String) -> Unit,
    onSaveDialogSubmitted: () -> Unit,
    onSaveDialogCancelled: () -> Unit,
    deviceSelected: (Device) -> Unit,
    onSave: () -> Unit,
    onAction: (Device, ActionToExecute) -> Unit
) {

    if (showSaveDialog) {
        ConfirmTextDialog(
            title = "Choose group name",
            hint = "Enter name",
            text = saveDialogInputValue,
            onTextChanged = onSaveDialogInputChanged,
            onConfirm = onSaveDialogSubmitted,
            onDismiss = onSaveDialogCancelled
        )
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(visible = selectedDevices.isNotEmpty(), enter = scaleIn(), exit = scaleOut()) {
                FloatingActionButton(onClick = { onSave() }) {
                    Icon(imageVector = Icons.Filled.Save, contentDescription = "Save selected actions")
                }
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item(key = "dropdown") { DeviceSelectionDropdown(devices, selectedDevices) { device -> deviceSelected(device) } }
            itemsIndexed(
                items = selectedDevices,
                key = { _, deviceId -> deviceId }
            ) { index, deviceId ->
                val device = devices.first { device -> device.id == deviceId }
                RaffstoreListItem(
                    device = device,
                    executing = false,
                    showExecutionIcon = false,
                    showTopDivider = index == 0,
                    showBottomDivider = index != selectedDevices.lastIndex,
                    showFavourite = false,
                    onAction = { action -> onAction(device, action) }
                )
            }
        }
    }
}