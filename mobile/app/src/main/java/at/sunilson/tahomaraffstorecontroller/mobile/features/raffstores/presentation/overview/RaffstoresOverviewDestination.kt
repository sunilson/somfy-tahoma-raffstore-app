package at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.presentation.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.lifecycle.OnPause
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.lifecycle.OnResume
import org.koin.androidx.compose.koinViewModel

@Composable
fun RaffstoresOverviewDestination() {
    val viewModel = koinViewModel<RaffstoresOverviewViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    OnPause { viewModel.onViewPaused() }
    OnResume { viewModel.onViewResumed() }

    RaffstoresOverviewScreen(
        refreshing = uiState.loading,
        devices = uiState.filteredDevices,
        favouriteDevices = uiState.favouriteDevices,
        onFavouriteClicked = viewModel::onFavouriteClicked,
        onItemClicked = viewModel::onItemClicked,
        executions = uiState.executions,
        searchTerm = uiState.searchTerm,
        onSearchTermChanged = viewModel::onSearchTermChanged,
        onRefreshRequested = viewModel::onRefreshRequested
    )
}

fun NavController.navigateToRaffstoresGraph() = navigate("raffstores")