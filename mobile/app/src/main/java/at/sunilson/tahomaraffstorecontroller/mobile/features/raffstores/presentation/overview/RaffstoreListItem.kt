package at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.presentation.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Blinds
import androidx.compose.material.icons.filled.BlindsClosed
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Texture
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.ActionToExecute
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.DeviceAction
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.TimeUtils
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.preview.DevicePreviewUtils
import kotlin.math.roundToInt

@Composable
fun RaffstoreListItem(
    modifier: Modifier = Modifier,
    device: Device,
    executing: Boolean,
    showBottomDivider: Boolean = false,
    showTopDivider: Boolean = false,
    showExecutionIcon: Boolean = true,
    showFavourite: Boolean = true,
    isFavourite: Boolean = false,
    onAction: (ActionToExecute) -> Unit = {},
    onFavouriteClicked: (Device) -> Unit = {}
) {

    var debugClickCounter: Int by rememberSaveable { mutableStateOf(0) }
    var orientation: Int by rememberSaveable { mutableStateOf(device.slateOrientation ?: 0) }
    var closedPercentage: Int by rememberSaveable { mutableStateOf(device.closedPercentage) }
    var lastOrientationChange: Long by rememberSaveable { mutableStateOf(0L) }
    var lastClosedPercentageChange: Long by rememberSaveable { mutableStateOf(0L) }

    LaunchedEffect(device.slateOrientation) {
        // Ignore new orientation if it is very close to previous one to reduce jumping
        if (device.slateOrientation in (orientation - 2..orientation + 2)) return@LaunchedEffect

        // Dont update while moving. When other devices are updated at the same time it may trigger a reload and change it back
        // to original position. I would rather have it at user set value while moving
        if (device.isMoving) return@LaunchedEffect

        // Only accept new remote values 1 second after user changed it to reduce jumping
        if (TimeUtils.getCurrentMillis() - lastOrientationChange < 1000) return@LaunchedEffect

        orientation = device.slateOrientation ?: return@LaunchedEffect
    }

    LaunchedEffect(device.closedPercentage) {
        // Ignore new orientation if it is very close to previous one to reduce jumping
        if (device.closedPercentage in (closedPercentage - 2..closedPercentage + 2)) return@LaunchedEffect

        // Dont update while moving. When other devices are updated at the same time it may trigger a reload and change it back
        // to original position. I would rather have it at user set value while moving
        if (device.isMoving) return@LaunchedEffect

        // Only accept new remote values 1 second after user changed it to reduce jumping
        if (TimeUtils.getCurrentMillis() - lastClosedPercentageChange < 1000) return@LaunchedEffect

        closedPercentage = device.closedPercentage
    }

    if (showTopDivider) {
        Spacer(modifier = Modifier.padding(6.dp))
        Divider()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = device.name,
                style = MaterialTheme.typography.titleMedium,
                color = if (device.controllable) MaterialTheme.colorScheme.onSurface else Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .clickable { debugClickCounter++ }
            )
            if (showFavourite) {
                IconButton(
                    onClick = { onFavouriteClicked(device) },
                    enabled = device.controllable
                ) {
                    if (isFavourite) {
                        Icon(Icons.Default.Favorite, contentDescription = "Remove favorite")
                    } else {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                    }
                }
            }
            IconButton(
                onClick = { onAction(DeviceAction.Open(device.id)) },
                enabled = device.controllable
            ) { Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Open Raffstore") }
            IconButton(
                onClick = { onAction(DeviceAction.Close(device.id)) },
                enabled = device.controllable
            ) { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Close Raffstore") }
            if (showExecutionIcon) {
                IconButton(
                    onClick = { onAction(DeviceAction.Stop(device.id)) },
                    enabled = device.isMoving && device.controllable
                ) { Icon(Icons.Default.Stop, contentDescription = "Stop execution") }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Texture,
                contentDescription = "",
                modifier = Modifier.clickable { onAction(DeviceAction.MyPosition(device.id)) })
            Spacer(modifier = Modifier.width(12.dp))
            Slider(
                value = orientation.toFloat(),
                valueRange = 0f..100f,
                steps = 100,
                enabled = device.controllable,
                modifier = Modifier.weight(1f),
                onValueChange = { orientation = it.roundToInt() },
                onValueChangeFinished = {
                    if (orientation == device.slateOrientation) return@Slider
                    lastOrientationChange = TimeUtils.getCurrentMillis()
                    onAction(DeviceAction.SetClosureAndOrientation(device.id, closedPercentage, orientation))
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "$orientation %", style = MaterialTheme.typography.bodyMedium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (device.isClosed) Icons.Filled.BlindsClosed else Icons.Filled.Blinds,
                contentDescription = "State of raffstore"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Slider(
                value = closedPercentage.toFloat(),
                valueRange = 0f..100f,
                steps = 100,
                enabled = device.controllable,
                modifier = Modifier.weight(1f),
                onValueChange = { closedPercentage = it.roundToInt() },
                onValueChangeFinished = {
                    if (closedPercentage == device.closedPercentage) return@Slider
                    lastClosedPercentageChange = TimeUtils.getCurrentMillis()
                    onAction(DeviceAction.SetClosureAndOrientation(device.id, closedPercentage, orientation))
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "$closedPercentage %", style = MaterialTheme.typography.bodyMedium)
        }

        if (debugClickCounter >= 5) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { onAction(DeviceAction.RunManufacturerSettingsCommand.DoublePowerCut(device.id)) }) {
                    Text(text = "PowerCut")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(onClick = { onAction(DeviceAction.RunManufacturerSettingsCommand.SaveMyPosition(device.id)) }) {
                    Text(text = "MyPosition")
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(6.dp))
    if (showBottomDivider) Divider()
    Spacer(modifier = Modifier.padding(6.dp))
}

@Composable
@Preview(showBackground = true)
private fun DefaultPreview() {
    RaffstoreListItem(
        device = DevicePreviewUtils.default("Raffstore Item"),
        executing = false
    )
}


@Composable
@Preview(showBackground = true)
private fun FavouritePreview() {
    RaffstoreListItem(
        device = DevicePreviewUtils.default("Raffstore Item"),
        isFavourite = true,
        executing = false
    )
}

@Composable
@Preview(showBackground = true)
private fun UnavailablePreview() {
    RaffstoreListItem(
        device = DevicePreviewUtils.unavailable("Raffstore Item"),
        executing = false
    )
}

@Composable
@Preview(showBackground = true)
private fun MovingPreview() {
    RaffstoreListItem(
        device = DevicePreviewUtils.moving("Raffstore Item"),
        executing = true
    )
}