package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview

import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.GetSchedulesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class SchedulesOverviewViewModel(private val getSchedulesUseCase: GetSchedulesUseCase) :
    BaseViewModel<SchedulesOverviewViewModel.State, SchedulesOverviewViewModel.SideEffects>(State()) {

    init {
        collectSchedules()
    }

    private fun collectSchedules() {
        viewModelScope.launch {
            getSchedulesUseCase(Unit).collect { schedules ->
                reduce { it.copy(schedules = schedules.toImmutableList()) }
            }
        }
    }

    data class State(val schedules: ImmutableList<Schedule> = emptyList<Schedule>().toImmutableList())
    sealed interface SideEffects
}