package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.overview

import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.entities.StopActionGroupExecution
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.DeleteActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.FavouriteGroupUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetActionGroupsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetFavouriteGroups
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ExecuteLocalApiAction
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ExecuteActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ObserveRemoteExecutionsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.RefreshExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class GroupsOverviewViewModel(
    private val getActionGroupsUseCase: GetActionGroupsUseCase,
    private val getExecutions: ObserveRemoteExecutionsUseCase,
    private val refreshExecutions: RefreshExecutions,
    private val executeActionGroup: ExecuteActionGroup,
    private val executeLocalApiAction: ExecuteLocalApiAction,
    private val deleteActionGroup: DeleteActionGroup,
    private val favouriteGroupUseCase: FavouriteGroupUseCase,
    private val getFavouriteGroups: GetFavouriteGroups
) : BaseViewModel<GroupsOverviewViewModel.State, GroupsOverviewViewModel.SideEffect>(State()) {
    sealed interface SideEffect
    data class State(
        val actionGroups: ImmutableList<ExecutionActionGroup> = emptyList<ExecutionActionGroup>().toImmutableList(),
        val executions: ImmutableList<Execution> = emptyList<Execution>().toImmutableList(),
        val favouriteGroups: ImmutableList<String> = emptyList<String>().toImmutableList()
    )

    init {
        viewModelScope.launch { refreshExecutions(Unit) }
        observeActionGroups()
        observeExecutions()
        observeFavouriteGroups()
    }

    fun onExecuteActionGroupClicked(actionGroup: ExecutionActionGroup) {
        viewModelScope.launch { executeActionGroup(actionGroup.id) }
    }

    fun onStopExecutionClicked(actionGroup: ExecutionActionGroup) {
        viewModelScope.launch { executeLocalApiAction(ExecuteLocalApiAction.Params(StopActionGroupExecution(actionGroup))) }
    }

    fun onDeleteActionGroupClicked(actionGroup: ExecutionActionGroup) {
        viewModelScope.launch { deleteActionGroup(actionGroup.id) }
    }

    fun onFavouriteClicked(actionGroup: ExecutionActionGroup) {
        viewModelScope.launch { favouriteGroupUseCase(actionGroup.id) }
    }

    private fun observeExecutions() {
        viewModelScope.launch {
            getExecutions(Unit).collect { executions ->
                reduce { it.copy(executions = executions.toImmutableList()) }
            }
        }
    }

    private fun observeFavouriteGroups() {
        viewModelScope.launch {
            getFavouriteGroups(Unit).collect { favouriteGroups ->
                reduce { it.copy(favouriteGroups = favouriteGroups.map { it.id }.toImmutableList()) }
            }
        }
    }

    private fun observeActionGroups() {
        viewModelScope.launch {
            getActionGroupsUseCase(Unit).collect { actionGroups ->
                reduce { it.copy(actionGroups = actionGroups.toImmutableList()) }
            }
        }
    }

}