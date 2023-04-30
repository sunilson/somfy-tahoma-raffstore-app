package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.overview

import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.DeleteActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.FavouriteGroupUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetActionGroups
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetFavouriteGroups
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.ExecuteAction
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.ExecuteActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.GetExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.RefreshExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.StopActionGroupExecution
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class GroupsOverviewViewModel(
    private val getActionGroups: GetActionGroups,
    private val getExecutions: GetExecutions,
    private val refreshExecutions: RefreshExecutions,
    private val executeActionGroup: ExecuteActionGroup,
    private val executeAction: ExecuteAction,
    private val deleteActionGroup: DeleteActionGroup,
    private val favouriteGroupUseCase: FavouriteGroupUseCase,
    private val getFavouriteGroups: GetFavouriteGroups
) : BaseViewModel<GroupsOverviewViewModel.State, GroupsOverviewViewModel.SideEffect>(State()) {
    sealed interface SideEffect
    data class State(
        val actionGroups: ImmutableList<LocalExecutionActionGroup> = emptyList<LocalExecutionActionGroup>().toImmutableList(),
        val executions: ImmutableList<Execution> = emptyList<Execution>().toImmutableList(),
        val favouriteGroups: ImmutableList<String> = emptyList<String>().toImmutableList()
    )

    init {
        viewModelScope.launch { refreshExecutions(Unit) }
        observeActionGroups()
        observeExecutions()
        observeFavouriteGroups()
    }

    fun onExecuteActionGroupClicked(actionGroup: LocalExecutionActionGroup) {
        viewModelScope.launch { executeActionGroup(actionGroup.id) }
    }

    fun onStopExecutionClicked(actionGroup: LocalExecutionActionGroup) {
        viewModelScope.launch { executeAction(StopActionGroupExecution(actionGroup)) }
    }

    fun onDeleteActionGroupClicked(actionGroup: LocalExecutionActionGroup) {
        viewModelScope.launch { deleteActionGroup(actionGroup.id) }
    }

    fun onFavouriteClicked(actionGroup: LocalExecutionActionGroup) {
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
            getActionGroups(Unit).collect { actionGroups ->
                reduce { it.copy(actionGroups = actionGroups.toImmutableList()) }
            }
        }
    }

}