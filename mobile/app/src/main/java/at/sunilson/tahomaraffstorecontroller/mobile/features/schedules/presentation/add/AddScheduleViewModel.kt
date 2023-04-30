package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add

import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel

class AddScheduleViewModel : BaseViewModel<AddScheduleViewModel.State, AddScheduleViewModel.SideEffect>(State.Empty) {

    sealed interface State {
        object Empty : State
    }
    sealed interface SideEffect

}