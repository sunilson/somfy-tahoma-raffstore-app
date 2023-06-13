package at.sunilson.tahomaraffstorecontroller.mobile.features.services.presentation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add.navigateToAddScheduleScreen
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview.SchedulesOverviewScreen
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview.SchedulesOverviewViewModel
import at.sunilson.tahomaraffstorecontroller.mobile.features.services.SchedulesForegroundService
import at.sunilson.tahomaraffstorecontroller.mobile.features.services.TahomaForegroundService
import at.sunilson.tahomaraffstorecontroller.mobile.main.ServicesCache
import org.koin.androidx.compose.koinViewModel

@Composable
fun ServicesOverviewDestination(navigator: NavController) {
    val isSchedulesServiceRunning by ServicesCache.isSchedulesServiceRunning.collectAsState()
    val isTahomaServiceRunning by ServicesCache.isTahomaServiceRunning.collectAsState()
    val context = LocalContext.current

    ServicesOverviewScreen(
        isSchedulesServiceRunning = isSchedulesServiceRunning,
        isTahomaServiceRunning = isTahomaServiceRunning,
        onToggleTahomaServiceClicked = {
            if (isTahomaServiceRunning) {
                context.stopService(Intent(context, TahomaForegroundService::class.java))
            } else {
                context.startService(Intent(context, TahomaForegroundService::class.java))
            }
        },
        onToggleScheduleServiceClicked = {
            if (isSchedulesServiceRunning) {
                context.stopService(Intent(context, SchedulesForegroundService::class.java))
            } else {
                context.startService(Intent(context, SchedulesForegroundService::class.java))
            }
        }
    )
}

fun NavController.navigateToServicesGraph() = navigate("services")