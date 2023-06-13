package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetActionGroupsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.UpsertActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add.AddGroupViewModel
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.GetScheduleUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.GetSchedulesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.UpsertScheduleUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

class AddScheduleViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getActionGroupsUseCase: GetActionGroupsUseCase,
    private val upsertScheduleUseCase: UpsertScheduleUseCase
) : BaseViewModel<AddScheduleViewModel.State, AddScheduleViewModel.SideEffect>(State()) {

    init {
        collectActionGroups()
        getSchedule()
    }

    fun onActionGroupClicked(actionGroup: ExecutionActionGroup) {
        reduce { state ->
            if (state.selectedGroups.contains(actionGroup.id)) {
                state.copy(selectedGroups = (state.selectedGroups - actionGroup.id).toImmutableSet())
            } else {
                state.copy(selectedGroups = (state.selectedGroups + actionGroup.id).toImmutableSet())
            }
        }
    }

    fun onDayOfWeekClicked(dayOfWeek: DayOfWeek) {
        reduce { state ->
            if (state.selectedDaysOfWeek.contains(dayOfWeek)) {
                state.copy(selectedDaysOfWeek = (state.selectedDaysOfWeek - dayOfWeek).toImmutableSet())
            } else {
                state.copy(selectedDaysOfWeek = (state.selectedDaysOfWeek + dayOfWeek).toImmutableSet())
            }
        }
    }

    fun onOnlyOnSunshineClicked(checked: Boolean) {
        reduce { state -> state.copy(onlyOnSunshine = checked) }
    }

    fun onTimeSelected(localTime: LocalTime) {
        reduce { state -> state.copy(selectedTime = localTime) }
    }

    fun onSaveDialogInputChanged(newInput: String) {
        reduce { it.copy(saveDialogInputValue = newInput) }
    }

    fun onSaveDialogCancelled() {
        reduce { it.copy(showSaveDialog = false, saveDialogInputValue = "") }
    }

    fun onSaveDialogSubmitted() {
        val label = state.value.saveDialogInputValue
        viewModelScope.launch {
            reduce { it.copy(showSaveDialog = false, saveDialogInputValue = "") }
            val state = state.value
            upsertScheduleUseCase(
                Schedule(
                    id = state.schedule?.id ?: UUID.randomUUID().toString(),
                    label = label,
                    weekdays = state.selectedDaysOfWeek.toList(),
                    hourOfDay = state.selectedTime.hour,
                    minuteOfHour = state.selectedTime.minute,
                    onlyWhenSunIsShining = state.onlyOnSunshine,
                    actionGroupIds = state.selectedGroups.toList()
                )
            )
            postSideEffect(SideEffect.Finished)
        }
    }

    fun onSaveButtonClicked() {
        reduce { state -> state.copy(showSaveDialog = true, saveDialogInputValue = state.schedule?.label.orEmpty()) }
    }

    private fun getSchedule() {
        viewModelScope.launch {
            val schedule = getScheduleUseCase(savedStateHandle["scheduleId"] ?: return@launch).getOrNull() ?: return@launch
            reduce { state ->
                state.copy(
                    schedule = schedule,
                    selectedTime = LocalTime.of(schedule.hourOfDay, schedule.minuteOfHour),
                    selectedDaysOfWeek = schedule.weekdays.toImmutableSet(),
                    selectedGroups = schedule.actionGroupIds.toImmutableSet(),
                    onlyOnSunshine = schedule.onlyWhenSunIsShining
                )
            }
        }
    }

    private fun collectActionGroups() {
        viewModelScope.launch {
            getActionGroupsUseCase(Unit).collect { actionGroups ->
                reduce { it.copy(actionGroups = actionGroups.toImmutableList()) }
            }
        }
    }

    data class State(
        val actionGroups: ImmutableList<ExecutionActionGroup> = emptyList<ExecutionActionGroup>().toImmutableList(),
        val selectedGroups: ImmutableSet<String> = emptySet<String>().toImmutableSet(),
        val selectedDaysOfWeek: ImmutableSet<DayOfWeek> = emptySet<DayOfWeek>().toImmutableSet(),
        val selectedTime: LocalTime = LocalTime.now(),
        val onlyOnSunshine: Boolean = false,
        val saveDialogInputValue: String = "",
        val showSaveDialog: Boolean = false,
        val schedule: Schedule? = null
    )

    sealed interface SideEffect {
        object Finished : SideEffect
    }
}