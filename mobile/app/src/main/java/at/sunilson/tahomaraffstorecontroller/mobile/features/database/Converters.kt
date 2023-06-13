package at.sunilson.tahomaraffstorecontroller.mobile.features.database

import androidx.room.TypeConverter
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecution
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecutionActionGroup
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Converters : KoinComponent {

    private val json: Json by inject()

    @TypeConverter
    fun deviceFromString(json: String): Device {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun deviceToString(device: Device): String {
        return this.json.encodeToString(device)
    }


    @TypeConverter
    fun devicesFromString(json: String): List<Device> {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun devicesToString(devices: List<Device>): String {
        return this.json.encodeToString(devices)
    }

    @TypeConverter
    fun executionFromString(json: String): LocalApiExecution {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun executionToString(localApiExecution: LocalApiExecution): String {
        return this.json.encodeToString(localApiExecution)
    }

    @TypeConverter
    fun executionActionGroupFromString(json: String): LocalApiExecutionActionGroup {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun executionActionGroupToString(localApiExecutionActionGroup: LocalApiExecutionActionGroup): String {
        return this.json.encodeToString(localApiExecutionActionGroup)
    }

    @TypeConverter
    fun localExecutionActionGroupFromString(json: String): ExecutionActionGroup {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun localExecutionActionGroupToString(executionActionGroup: ExecutionActionGroup): String {
        return this.json.encodeToString(executionActionGroup)
    }

    @TypeConverter
    fun actionsFromString(json: String): List<LocalApiExecutionActionGroup.Action> {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun actionsToString(actions: List<LocalApiExecutionActionGroup.Action>): String {
        return this.json.encodeToString(actions)
    }
}