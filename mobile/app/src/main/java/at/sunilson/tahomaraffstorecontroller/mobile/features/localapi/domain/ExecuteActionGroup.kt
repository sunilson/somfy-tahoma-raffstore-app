package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.ApiExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.DeviceAction.SetClosureAndOrientation
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class ExecuteActionGroup(
    private val tahomaLocalDatabase: TahomaLocalDatabase,
    private val tahomaLocalApi: TahomaLocalApi
) : UseCase<String, Unit>() {
    override suspend fun doWork(params: String) {
        val localActionGroup = tahomaLocalDatabase.dao().getExecutionActionGroupOnce(params)!!
        val apiActionGroup = ApiExecutionActionGroup(
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
                    ApiExecutionActionGroup.Action(device.id, commands)
                }
        )
        val executionId = tahomaLocalApi.executeCommands(apiActionGroup).execId

        tahomaLocalDatabase.dao().upsertExecution(Execution(executionId, "Me", apiActionGroup))

        localActionGroup.targetDeviceStates.forEach { device -> tahomaLocalDatabase.dao().setDeviceIsMoving(device.id, true) }
    }
}