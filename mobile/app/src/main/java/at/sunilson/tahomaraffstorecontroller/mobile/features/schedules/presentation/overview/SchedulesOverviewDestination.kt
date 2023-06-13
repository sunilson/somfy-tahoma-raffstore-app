package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import at.sunilson.tahomaraffstorecontroller.mobile.features.services.SchedulesForegroundService
import at.sunilson.tahomaraffstorecontroller.mobile.main.ServicesCache
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add.navigateToAddScheduleScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun SchedulesOverviewDestination(navigator: NavController) {
    val viewModel = koinViewModel<SchedulesOverviewViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SchedulesOverviewScreen(
        schedules = uiState.schedules,
        onScheduleClicked = { navigator.navigateToAddScheduleScreen(it) },
        onAddButtonClicked = { navigator.navigateToAddScheduleScreen() },
    )
}

fun NavController.navigateToSchedulesGraph() = navigate("schedules")