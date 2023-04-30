package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities

import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.ApiExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup

sealed interface ActionToExecute

object StopAllExecutions : ActionToExecute

data class StopDeviceExecutions(val deviceUrl: String) : ActionToExecute

data class StopActionGroupExecution(val actionGroup: LocalExecutionActionGroup) : ActionToExecute

sealed class DeviceAction(val commandName: String) : ActionToExecute {
    abstract val deviceUrl: String
    open val command: ApiExecutionActionGroup.Action.Command = ApiExecutionActionGroup.Action.Command.EmptyCommand(commandName)

    sealed class RunManufacturerSettingsCommand(
        private val manufacturerCommand: String
    ) : DeviceAction("runManufacturerSettingsCommand") {

        override val command: ApiExecutionActionGroup.Action.Command
            get() = ApiExecutionActionGroup.Action.Command.StringCommand(commandName, listOf(manufacturerCommand, manufacturerCommand))

        data class EnterSettingsMode(override val deviceUrl: String) : RunManufacturerSettingsCommand("enter_settings_mode")
        data class ExitSettingsMode(override val deviceUrl: String) : RunManufacturerSettingsCommand("eject_from_setting_mode")
        data class DoublePowerCut(override val deviceUrl: String) : RunManufacturerSettingsCommand("double_power_cut")
        data class SaveSettings(override val deviceUrl: String) : RunManufacturerSettingsCommand("save_settings")
        data class SaveMyPosition(override val deviceUrl: String) : RunManufacturerSettingsCommand("save_my_position")
    }

    data class Stop(override val deviceUrl: String) : DeviceAction("stop")

    data class Open(override val deviceUrl: String) : DeviceAction("up")

    data class Close(override val deviceUrl: String) : DeviceAction("down")

    data class MyPosition(override val deviceUrl: String) : DeviceAction("my")

    data class SetClosureAndOrientation(
        override val deviceUrl: String,
        @androidx.annotation.IntRange(0, 100) val closedPercentage: Int,
        @androidx.annotation.IntRange(0, 100) val orientation: Int
    ) : DeviceAction("setClosureAndOrientation") {
        override val command: ApiExecutionActionGroup.Action.Command
            get() = ApiExecutionActionGroup.Action.Command.IntCommand(commandName, listOf(closedPercentage, orientation))
    }

    data class SetClosedPercentage(
        override val deviceUrl: String,
        @androidx.annotation.IntRange(0, 100) val closedPercentage: Int
    ) : DeviceAction("setClosure") {
        override val command: ApiExecutionActionGroup.Action.Command
            get() = ApiExecutionActionGroup.Action.Command.IntCommand(commandName, listOf(closedPercentage))
    }

    data class SetOrientation(
        override val deviceUrl: String,
        @androidx.annotation.IntRange(0, 100) val orientation: Int
    ) : DeviceAction("setOrientation") {
        override val command: ApiExecutionActionGroup.Action.Command
            get() = ApiExecutionActionGroup.Action.Command.IntCommand(commandName, listOf(orientation))
    }

    data class PairController(override val deviceUrl: String) : DeviceAction("unpairAllOneWayControllers")

    data class SendIOKey(override val deviceUrl: String) : DeviceAction("sendIOKey")

    fun toApiAction() = ApiExecutionActionGroup.Action(
        deviceURL = this.deviceUrl,
        commands = listOf(command)
    )
}