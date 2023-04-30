package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddGroupDestination(navigator: NavController) {
    val viewModel = koinViewModel<AddGroupViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    viewModel.collectSideEffect {
        when (it) {
            AddGroupViewModel.SideEffect.Finished -> navigator.popBackStack()
        }
    }

    AddGroupScreen(
        devices = uiState.devices,
        selectedDevices = uiState.selectedDevices,
        showSaveDialog = uiState.showSaveDialog,
        saveDialogInputValue = uiState.saveDialogInputValue,
        onSaveDialogInputChanged = viewModel::onSaveDialogInputChanged,
        onSaveDialogCancelled = viewModel::onSaveDialogCancelled,
        onSaveDialogSubmitted = viewModel::onSaveDialogSubmitted,
        deviceSelected = viewModel::onDeviceSelected,
        onSave = viewModel::onSaveClicked,
        onAction = viewModel::onDeviceActionExecuted,
    )
}

fun NavController.navigateToAddGroupScreen(groupId: String? = null) = if (groupId == null) {
    navigate("groups/add")
} else {
    navigate("groups/add?groupId=${groupId}")
}
