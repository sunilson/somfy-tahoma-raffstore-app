package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add.navigateToAddGroupScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun GroupsOverviewDestination(navigator: NavController) {
    val viewModel = koinViewModel<GroupsOverviewViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    GroupsOverviewScreen(
        actionGroups = uiState.actionGroups,
        executions = uiState.executions,
        favouriteGroups = uiState.favouriteGroups,
        onItemClicked = { navigator.navigateToAddGroupScreen(it.id) },
        onDeleteActionGroupClicked = viewModel::onDeleteActionGroupClicked,
        onStopExecutionClicked = viewModel::onStopExecutionClicked,
        onExecuteActionGroupClicked = viewModel::onExecuteActionGroupClicked,
        onAddButtonClick = { navigator.navigateToAddGroupScreen() },
        onFavouriteClicked = viewModel::onFavouriteClicked
    )
}

fun NavController.navigateToGroupsGraph() = navigate("groups")