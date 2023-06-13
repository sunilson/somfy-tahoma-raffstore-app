package at.sunilson.tahomaraffstorecontroller.mobile.features.services.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ServicesOverviewScreen(
    isSchedulesServiceRunning: Boolean = false,
    isTahomaServiceRunning: Boolean = false,
    onToggleScheduleServiceClicked: () -> Unit,
    onToggleTahomaServiceClicked: () -> Unit,
) {
    Column {
        Button(
            onClick = onToggleScheduleServiceClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Crossfade(targetState = isSchedulesServiceRunning, label = "isSchedulesServiceRunning") {
                if (it) {
                    Icon(
                        imageVector = Icons.Filled.Pause,
                        contentDescription = "Toggle schedule service"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Toggle schedule service"
                    )
                }
            }
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = if (isSchedulesServiceRunning) "Disable schedule service" else "Enable schedule service"
            )
        }
        Button(
            onClick = onToggleTahomaServiceClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Crossfade(targetState = isTahomaServiceRunning, label = "isTahomaServiceRunning") {
                if (it) {
                    Icon(
                        imageVector = Icons.Filled.Pause,
                        contentDescription = "Toggle tahoma service"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Toggle tahoma service"
                    )
                }
            }
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = if (isTahomaServiceRunning) "Disable tahoma service" else "Enable tahoma service"
            )
        }
    }
}