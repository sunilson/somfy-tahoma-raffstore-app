package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add.AddGroupViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddScheduleDestination(navigator: NavHostController) {
    val viewModel = koinViewModel<AddScheduleViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    viewModel.collectSideEffect {
        when (it) {
            AddScheduleViewModel.SideEffect.Finished -> navigator.popBackStack()
        }
    }

    AddScheduleScreen(
        allActionGroups = uiState.actionGroups,
        selectedActionGroups = uiState.selectedGroups,
        selectedDaysOfWeek = uiState.selectedDaysOfWeek,
        selectedTime = uiState.selectedTime,
        onlyOnSunshine = uiState.onlyOnSunshine,
        onActionGroupClicked = viewModel::onActionGroupClicked,
        onDayOfWeekClicked = viewModel::onDayOfWeekClicked,
        onSaveButtonClicked = viewModel::onSaveButtonClicked,
        onOnlyOnSunshineClicked = viewModel::onOnlyOnSunshineClicked,
        onTimeSelected = viewModel::onTimeSelected,
        showSaveDialog = uiState.showSaveDialog,
        saveDialogInputValue = uiState.saveDialogInputValue,
        onSaveDialogInputChanged = viewModel::onSaveDialogInputChanged,
        onSaveDialogCancelled = viewModel::onSaveDialogCancelled,
        onSaveDialogSubmitted = viewModel::onSaveDialogSubmitted,
    )
}

fun NavController.navigateToAddScheduleScreen(scheduleId: String? = null) = if (scheduleId == null) {
    navigate("schedules/add")
} else {
    navigate("schedules/add?scheduleId=${scheduleId}")
}
