package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.ApiExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.ActionToExecute
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.DeviceAction
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.StopActionGroupExecution
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.StopAllExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.StopDeviceExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.forEachParallel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import timber.log.Timber

class ExecuteAction(
    private val tahomaLocalApi: TahomaLocalApi,
    private val tahomaLocalDatabase: TahomaLocalDatabase,
    private val loadDevicesUseCase: LoadDevicesUseCase,
    private val updateDeviceStatesUseCase: UpdateDeviceStatesUseCase,
    private val refreshExecutions: RefreshExecutions,
) : UseCase<ActionToExecute, Unit>() {

    override suspend fun doWork(params: ActionToExecute) {
        Timber.d("Executing action $params")
        when (params) {
            is DeviceAction -> executeDeviceAction(params)
            is StopAllExecutions -> stopAllExecutions()
            is StopDeviceExecutions -> stopDeviceExecution(params.deviceUrl)
            is StopActionGroupExecution -> stopActionGroupExecution(params.actionGroup)
        }

        delay(1000)
        refreshExecutions(Unit)
        loadDevicesUseCase(Unit)
    }

    private suspend fun stopAllExecutions() {
        tahomaLocalApi.stopAllExecutions()
        loadDevicesUseCase(Unit)

        val allDevices = tahomaLocalDatabase.dao().getAllDevices().first()
        allDevices.forEach { device -> tahomaLocalDatabase.dao().setDeviceIsMoving(device.id, false) }
    }

    private suspend fun stopActionGroupExecution(actionGroup: LocalExecutionActionGroup) {
        val stopActions = actionGroup.targetDeviceStates
            .map { DeviceAction.Stop(it.id) }
            .map { it.toApiAction() }

        tahomaLocalApi.executeCommands(
            ApiExecutionActionGroup(
                label = "Stopping execution for ${actionGroup.label}",
                actions = stopActions
            )
        )

        stopActions.forEach { action -> tahomaLocalDatabase.dao().setDeviceIsMoving(action.deviceURL, false) }
    }

    private suspend fun stopDeviceExecution(deviceUrl: String) {
        val executions = tahomaLocalDatabase.dao()
            .getAllExecutionsOnce()
            .filter { execution -> execution.actionGroup.actions.any { action -> action.deviceURL == deviceUrl } }
        Timber.d("Stopping executions ${executions.map { it.id }}")
        executions.forEachParallel { tahomaLocalApi.stopExecution(it.id) }
    }

    private suspend fun executeDeviceAction(deviceAction: DeviceAction) {
        val actionGroup = ApiExecutionActionGroup(
            label = "Single execution",
            actions = listOf(deviceAction.toApiAction())
        )

        tahomaLocalApi.executeCommands(actionGroup).execId

        if (deviceAction !is DeviceAction.Stop) {
            tahomaLocalDatabase.dao().setDeviceIsMoving(deviceAction.deviceUrl, true)
        } else {
            tahomaLocalDatabase.dao().setDeviceIsMoving(deviceAction.deviceUrl, false)
        }
    }
}