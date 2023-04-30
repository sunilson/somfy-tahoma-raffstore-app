package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview

import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel

class SchedulesOverviewViewModel : BaseViewModel<SchedulesOverviewViewModel.State, SchedulesOverviewViewModel.SideEffects>(State()) {
    data class State(val schedules: List<Any> = emptyList())
    sealed interface SideEffects
}