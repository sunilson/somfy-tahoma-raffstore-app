package at.sunilson.tahomaraffstorecontroller.mobile.entities

import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecutionActionGroup
import kotlinx.serialization.Serializable

@Serializable
sealed interface ActionToExecute

@Serializable
object StopAllExecutions : ActionToExecute

@Serializable
data class StopDeviceExecutions(val deviceUrl: String) : ActionToExecute

@Serializable
data class StopActionGroupExecution(val actionGroup: ExecutionActionGroup) : ActionToExecute

@Serializable
sealed class DeviceAction(val commandName: String) : ActionToExecute {
    abstract val deviceUrl: String
    open val command: LocalApiExecutionActionGroup.Action.Command = LocalApiExecutionActionGroup.Action.Command.EmptyCommand(commandName)

    @Serializable
    sealed class RunManufacturerSettingsCommand(
        private val manufacturerCommand: String
    ) : DeviceAction("runManufacturerSettingsCommand") {

        override val command: LocalApiExecutionActionGroup.Action.Command
            get() = LocalApiExecutionActionGroup.Action.Command.StringCommand(commandName, listOf(manufacturerCommand, manufacturerCommand))

        @Serializable
        data class EnterSettingsMode(override val deviceUrl: String) : RunManufacturerSettingsCommand("enter_settings_mode")

        @Serializable
        data class ExitSettingsMode(override val deviceUrl: String) : RunManufacturerSettingsCommand("eject_from_setting_mode")

        @Serializable
        data class DoublePowerCut(override val deviceUrl: String) : RunManufacturerSettingsCommand("double_power_cut")

        @Serializable
        data class SaveSettings(override val deviceUrl: String) : RunManufacturerSettingsCommand("save_settings")

        @Serializable
        data class SaveMyPosition(override val deviceUrl: String) : RunManufacturerSettingsCommand("save_my_position")
    }

    @Serializable
    data class Stop(override val deviceUrl: String) : DeviceAction("stop")

    @Serializable
    data class Open(override val deviceUrl: String) : DeviceAction("up")

    @Serializable
    data class Close(override val deviceUrl: String) : DeviceAction("down")

    @Serializable
    data class MyPosition(override val deviceUrl: String) : DeviceAction("my")

    @Serializable
    data class SetClosureAndOrientation(
        override val deviceUrl: String,
        @androidx.annotation.IntRange(0, 100) val closedPercentage: Int,
        @androidx.annotation.IntRange(0, 100) val orientation: Int
    ) : DeviceAction("setClosureAndOrientation") {
        override val command: LocalApiExecutionActionGroup.Action.Command
            get() = LocalApiExecutionActionGroup.Action.Command.IntCommand(commandName, listOf(closedPercentage, orientation))
    }

    @Serializable
    data class SetClosedPercentage(
        override val deviceUrl: String,
        @androidx.annotation.IntRange(0, 100) val closedPercentage: Int
    ) : DeviceAction("setClosure") {
        override val command: LocalApiExecutionActionGroup.Action.Command
            get() = LocalApiExecutionActionGroup.Action.Command.IntCommand(commandName, listOf(closedPercentage))
    }

    @Serializable
    data class SetOrientation(
        override val deviceUrl: String,
        @androidx.annotation.IntRange(0, 100) val orientation: Int
    ) : DeviceAction("setOrientation") {
        override val command: LocalApiExecutionActionGroup.Action.Command
            get() = LocalApiExecutionActionGroup.Action.Command.IntCommand(commandName, listOf(orientation))
    }

    @Serializable
    data class PairController(override val deviceUrl: String) : DeviceAction("unpairAllOneWayControllers")

    @Serializable
    data class SendIOKey(override val deviceUrl: String) : DeviceAction("sendIOKey")

    fun toApiAction() = LocalApiExecutionActionGroup.Action(
        deviceURL = this.deviceUrl,
        commands = listOf(command)
    )
}