package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun SchedulesOverviewDestination(navigator: NavController) {
    val viewModel = koinViewModel<SchedulesOverviewViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    SchedulesOverviewScreen(
        onAddButtonClick = {}
    )
}

fun NavController.navigateToSchedulesGraph() = navigate("schedules")