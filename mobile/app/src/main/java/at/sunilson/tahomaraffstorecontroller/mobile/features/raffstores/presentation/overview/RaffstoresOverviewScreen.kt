package at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.presentation.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.ActionToExecute
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.StopAllExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.components.AutoFocusTextField
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.preview.DevicePreviewUtils
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun RaffstoresOverviewScreen(
    refreshing: Boolean,
    searchTerm: String,
    devices: ImmutableList<Device>,
    favouriteDevices: ImmutableList<String>,
    executions: ImmutableList<Execution>,
    onItemClicked: (ActionToExecute) -> Unit,
    onFavouriteClicked: (Device) -> Unit,
    onSearchTermChanged: (String) -> Unit,
    onRefreshRequested: () -> Unit
) {
    Scaffold(floatingActionButton = {
        AnimatedVisibility(
            visible = devices.any { it.isMoving },
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            FloatingActionButton(onClick = { onItemClicked(StopAllExecutions) }) {
                Icon(imageVector = Icons.Filled.Stop, contentDescription = "Stop all executions")
            }
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            item(key = "input") {
                AutoFocusTextField(
                    value = searchTerm,
                    placeholder = { Text(text = "Enter search term") },
                    onValueChange = onSearchTermChanged
                )
            }
            itemsIndexed(
                items = devices,
                key = { _, device -> device.id }
            ) { index, device ->
                RaffstoreListItem(
                    device = device,
                    executing = device.isMoving,
                    onAction = onItemClicked,
                    showBottomDivider = index != devices.lastIndex,
                    onFavouriteClicked = onFavouriteClicked,
                    isFavourite = favouriteDevices.contains(device.id)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DefaultPreview() {
    RaffstoresOverviewScreen(
        refreshing = false,
        searchTerm = "",
        devices = listOf(
            DevicePreviewUtils.default("Device 1"),
            DevicePreviewUtils.moving("Device 2"),
            DevicePreviewUtils.unavailable("Device 3")
        ).toImmutableList(),
        favouriteDevices = emptyList<String>().toImmutableList(),
        executions = emptyList<Execution>().toImmutableList(),
        onItemClicked = {},
        onFavouriteClicked = {},
        onSearchTermChanged = {}
    ) {
    }
}