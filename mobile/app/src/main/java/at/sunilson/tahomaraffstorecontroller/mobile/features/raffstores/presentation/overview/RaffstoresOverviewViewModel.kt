package at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.presentation.overview

import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ActionToExecute
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.domain.FavouriteDeviceUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.domain.GetFavouriteDeviceIdsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ExecuteLocalApiAction
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ExecuteRemoteActionUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ObserveRemoteExecutionsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.LoadAndSyncDevicesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ObserveRemoteDevicesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class RaffstoresOverviewViewModel(
    private val observeRemoteDevicesUseCase: ObserveRemoteDevicesUseCase,
    private val getExecutions: ObserveRemoteExecutionsUseCase,
    private val executeRemoteActionUseCase: ExecuteRemoteActionUseCase,
    private val favouriteDeviceUseCase: FavouriteDeviceUseCase,
    private val getFavouriteDeviceIdsUseCase: GetFavouriteDeviceIdsUseCase,
    private val loadAndSyncDevicesUseCase: LoadAndSyncDevicesUseCase
) : BaseViewModel<RaffstoresOverviewViewModel.State, RaffstoresOverviewViewModel.SideEffect>(State()) {

    sealed interface SideEffect
    data class State(
        val devices: ImmutableList<Device> = emptyList<Device>().toImmutableList(),
        val filteredDevices: ImmutableList<Device> = emptyList<Device>().toImmutableList(),
        val favouriteDevices: ImmutableList<String> = emptyList<String>().toImmutableList(),
        val executions: ImmutableList<Execution> = emptyList<Execution>().toImmutableList(),
        val searchTerm: String = "",
        val loading: Boolean = false
    )

    init {
        collectDevices()
        collectExecutions()
        collectFavouriteDevices()
    }

    fun onViewResumed() {
        Timber.d("Resumed")
    }

    fun onViewPaused() {
        Timber.d("Paused")
    }

    fun onItemClicked(action: ActionToExecute) {
        viewModelScope.launch { executeRemoteActionUseCase(action) }
    }

    fun onFavouriteClicked(device: Device) {
        viewModelScope.launch { favouriteDeviceUseCase(device.id) }
    }

    private var searchJob: Job? = null
    fun onSearchTermChanged(searchTerm: String) {
        reduce { it.copy(searchTerm = searchTerm) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            reduce { it.copy(filteredDevices = it.devices.filterBySearchTerm(searchTerm).toImmutableList()) }
        }
    }

    fun onRefreshRequested() {
        viewModelScope.launch {
            reduce { it.copy(loading = true) }
            loadAndSyncDevicesUseCase(Unit)
            reduce { it.copy(loading = false) }
        }
    }

    private fun collectDevices() {
        viewModelScope.launch {
            observeRemoteDevicesUseCase(Unit).collect { devices ->
                reduce { it.copy(devices = devices.toImmutableList(), filteredDevices = devices.filterBySearchTerm(it.searchTerm).toImmutableList()) }
            }
        }
    }

    private fun List<Device>.filterBySearchTerm(searchTerm: String): List<Device> {
        return if (searchTerm.isEmpty()) {
            this
        } else {
            filter { it.name.contains(other = state.value.searchTerm, ignoreCase = true) }
        }
    }

    private fun collectExecutions() {
        viewModelScope.launch {
            getExecutions(Unit).collect { executions ->
                reduce { it.copy(executions = executions.toImmutableList()) }
            }
        }
    }

    private fun collectFavouriteDevices() {
        viewModelScope.launch {
            getFavouriteDeviceIdsUseCase(Unit).collect { favouriteDevices ->
                reduce { it.copy(favouriteDevices = favouriteDevices.toImmutableList()) }
            }
        }
    }
}