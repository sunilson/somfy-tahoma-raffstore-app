package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecution
import at.sunilson.tahomaraffstorecontroller.mobile.entities.DeviceAction.SetClosureAndOrientation
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.sanitizeFirebaseId
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.setValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase

class ExecuteActionGroup(
    private val tahomaLocalApi: TahomaLocalApi,
    private val firebaseDatabase: FirebaseDatabase,
    private val getActionGroup: GetActionGroup
) : UseCase<String, Unit>() {
    override suspend fun doWork(params: String) {
        val localActionGroup = getActionGroup(params).getOrThrow()
        val apiActionGroup = LocalApiExecutionActionGroup(
            label = localActionGroup.label,
            actions = localActionGroup.targetDeviceStates
                .map { device ->
                    val commands = buildList {
                        add(
                            SetClosureAndOrientation(
                                deviceUrl = device.id,
                                closedPercentage = device.closedPercentage,
                                orientation = device.slateOrientation
                            ).command
                        )
                    }
                    LocalApiExecutionActionGroup.Action(device.id, commands)
                }
        )
        val executionId = tahomaLocalApi.executeCommands(apiActionGroup).execId

        firebaseDatabase.reference
            .child("executions")
            .child(executionId)
            .setValueSuspending(
                Execution(
                    id = executionId,
                    actionGroupLabel = apiActionGroup.label,
                    deviceIds = apiActionGroup.actions.map { it.deviceURL }
                )
            )

        localActionGroup.targetDeviceStates
            .forEach { device ->
                firebaseDatabase.reference
                    .child("devices")
                    .child(device.id.sanitizeFirebaseId())
                    .child("isMoving")
                    .setValueSuspending(true)
            }
    }
}