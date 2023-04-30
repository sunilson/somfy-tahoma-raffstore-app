package at.sunilson.tahomaraffstorecontroller.mobile.features.database

import androidx.room.TypeConverter
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.ApiExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
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
    fun executionFromString(json: String): Execution {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun executionToString(execution: Execution): String {
        return this.json.encodeToString(execution)
    }

    @TypeConverter
    fun executionActionGroupFromString(json: String): ApiExecutionActionGroup {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun executionActionGroupToString(apiExecutionActionGroup: ApiExecutionActionGroup): String {
        return this.json.encodeToString(apiExecutionActionGroup)
    }

    @TypeConverter
    fun localExecutionActionGroupFromString(json: String): LocalExecutionActionGroup {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun localExecutionActionGroupToString(localExecutionActionGroup: LocalExecutionActionGroup): String {
        return this.json.encodeToString(localExecutionActionGroup)
    }

    @TypeConverter
    fun actionsFromString(json: String): List<ApiExecutionActionGroup.Action> {
        return this.json.decodeFromString(json)
    }

    @TypeConverter
    fun actionsToString(actions: List<ApiExecutionActionGroup.Action>): String {
        return this.json.encodeToString(actions)
    }
}