package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.ActionToExecute
import at.sunilson.tahomaraffstorecontroller.mobile.entities.DeviceAction
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.entities.StopActionGroupExecution
import at.sunilson.tahomaraffstorecontroller.mobile.entities.StopAllExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.entities.StopDeviceExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getListSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.removeValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.sanitizeFirebaseId
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.setValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.forEachParallel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import timber.log.Timber

class ExecuteLocalApiAction(
    private val tahomaLocalApi: TahomaLocalApi,
    private val loadAndSyncDevicesUseCase: LoadAndSyncDevicesUseCase,
    private val refreshExecutions: RefreshExecutions,
    private val firebaseDatabase: FirebaseDatabase,
) : UseCase<ExecuteLocalApiAction.Params, Unit>() {

    data class Params(val actionToExecute: ActionToExecute, val remoteChildId: String? = null)

    override suspend fun doWork(params: Params) {
        Timber.d("Executing action $params")
        when (params.actionToExecute) {
            is DeviceAction -> executeDeviceAction(params.actionToExecute)
            is StopAllExecutions -> stopAllExecutions()
            is StopDeviceExecutions -> stopDeviceExecution(params.actionToExecute.deviceUrl)
            is StopActionGroupExecution -> stopActionGroupExecution(params.actionToExecute.actionGroup)
        }

        delay(1000)
        refreshExecutions(Unit)
        loadAndSyncDevicesUseCase(Unit)
        deleteRemoteAction(params.remoteChildId ?: return)
    }

    private suspend fun stopAllExecutions() {
        tahomaLocalApi.stopAllExecutions()
        loadAndSyncDevicesUseCase(Unit)

        // TODO allDevices.forEach { device -> tahomaLocalDatabase.dao().setDeviceIsMoving(device.id, false) }
    }

    private suspend fun stopActionGroupExecution(actionGroup: ExecutionActionGroup) {
        val stopActions = actionGroup.targetDeviceStates
            .map { DeviceAction.Stop(it.id) }
            .map { it.toApiAction() }

        tahomaLocalApi.executeCommands(
            LocalApiExecutionActionGroup(
                label = "Stopping execution for ${actionGroup.label}",
                actions = stopActions
            )
        )

        stopActions.forEach { action ->
            firebaseDatabase.reference
                .child("devices")
                .child(action.deviceURL.sanitizeFirebaseId())
                .child("isMoving")
                .setValueSuspending(true)
        }
    }

    private suspend fun stopDeviceExecution(deviceId: String) {
        val executions = firebaseDatabase.reference.child("executions").getListSuspending<Execution>()
            .filter { execution -> execution.deviceIds.any { it == deviceId } }
        Timber.d("Stopping executions ${executions.map { it.id }}")
        executions.forEachParallel { tahomaLocalApi.stopExecution(it.id) }
    }

    private suspend fun executeDeviceAction(deviceAction: DeviceAction) {
        val actionGroup = LocalApiExecutionActionGroup(
            label = "Single execution",
            actions = listOf(deviceAction.toApiAction())
        )

        tahomaLocalApi.executeCommands(actionGroup).execId

        if (deviceAction !is DeviceAction.Stop) {
            firebaseDatabase.reference.child("devices").child(deviceAction.deviceUrl.sanitizeFirebaseId()).child("isMoving").setValueSuspending(true)
        } else {
            firebaseDatabase.reference.child("devices").child(deviceAction.deviceUrl.sanitizeFirebaseId()).child("isMoving").setValueSuspending(false)
        }
    }

    private suspend fun deleteRemoteAction(childNodeId: String) {
        firebaseDatabase.reference
            .child("actions")
            .child(childNodeId)
            .removeValueSuspending()
    }
}