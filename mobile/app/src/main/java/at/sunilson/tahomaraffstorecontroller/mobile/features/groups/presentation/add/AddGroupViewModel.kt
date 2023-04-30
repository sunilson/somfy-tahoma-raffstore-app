package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.ActionToExecute
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.CreateActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.GetRaffstoresUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.DeviceAction
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class AddGroupViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getRaffstoresUseCase: GetRaffstoresUseCase,
    private val getActionGroupUseCase: GetActionGroup,
    private val createActionGroup: CreateActionGroup,
) : BaseViewModel<AddGroupViewModel.State, AddGroupViewModel.SideEffect>(State()) {

    private val groupId: String? = savedStateHandle["groupId"]

    init {
        viewModelScope.launch {
            getDevices()
            if (groupId != null) {
                getActionGroup(groupId)
            }
        }

    }

    fun onSaveClicked() {
        reduce { it.copy(showSaveDialog = true, saveDialogInputValue = it.group?.label.orEmpty()) }
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
            createActionGroup(
                CreateActionGroup.Params(
                    label = label,
                    devicesInTargetState = state.value.devices.toList().filter { state.value.selectedDevices.contains(it.id) },
                    id = state.value.group?.id
                )
            )
            postSideEffect(SideEffect.Finished)
        }
    }

    fun onDeviceSelected(device: Device) {
        reduce { state ->
            val currentSelected = state.selectedDevices.toMutableList()
            if (currentSelected.contains(device.id)) {
                currentSelected.remove(device.id)
            } else {
                currentSelected.add(device.id)
            }
            state.copy(selectedDevices = currentSelected.toImmutableList())
        }
    }

    fun onDeviceActionExecuted(device: Device, actionToExecute: ActionToExecute) {
        when (actionToExecute) {
            is DeviceAction.SetClosureAndOrientation -> mutateDevice(device.id) {
                it.copy(
                    closedPercentage = actionToExecute.closedPercentage,
                    slateOrientation = actionToExecute.orientation
                )
            }

            is DeviceAction.Close -> mutateDevice(device.id) { it.copy(closedPercentage = 100, slateOrientation = 100) }
            is DeviceAction.Open -> mutateDevice(device.id) { it.copy(closedPercentage = 0, slateOrientation = 0) }
            is DeviceAction.SetOrientation -> mutateDevice(device.id) { it.copy(isClosed = true, slateOrientation = actionToExecute.orientation) }
            else -> Unit // Do nothing
        }
    }

    private suspend fun getActionGroup(groupId: String) {
        getActionGroupUseCase(groupId).fold(
            { result ->
                reduce { state ->
                    state.copy(
                        devices = state.devices.toMutableList().apply {
                            result.targetDeviceStates.forEach { targetDevice ->
                                val index = indexOf(targetDevice)
                                if (index != -1) set(index, targetDevice)
                            }
                        }.toImmutableList(),
                        selectedDevices = result.targetDeviceStates.map { it.id }.toImmutableList(),
                        group = result
                    )
                }
            },
            {
                Timber.e(it)
                postSideEffect(SideEffect.Finished)
            }
        )
    }

    private suspend fun getDevices() {
        val devices = getRaffstoresUseCase(Unit).first()
        reduce { it.copy(devices = devices.toImmutableList()) }
    }

    private fun mutateDevice(deviceId: String, block: (Device) -> Device) {
        reduce { state ->
            val indexOfDevice = state.devices.indexOfFirst { it.id == deviceId }
            val mutableDevices = state.devices.toMutableList()
            mutableDevices[indexOfDevice] = block(mutableDevices[indexOfDevice])
            Timber.d("""
                Mutated device $deviceId
                Orientation: ${mutableDevices[indexOfDevice].slateOrientation}
                Closure: ${mutableDevices[indexOfDevice].closedPercentage}
            """.trimIndent())
            state.copy(devices = mutableDevices.toImmutableList())
        }
    }

    sealed interface SideEffect {
        object Finished : SideEffect
    }

    data class State(
        val devices: ImmutableList<Device> = emptyList<Device>().toImmutableList(),
        val selectedDevices: ImmutableList<String> = emptyList<String>().toImmutableList(),
        val saveDialogInputValue: String = "",
        val showSaveDialog: Boolean = false,
        val group: LocalExecutionActionGroup? = null
    )
}