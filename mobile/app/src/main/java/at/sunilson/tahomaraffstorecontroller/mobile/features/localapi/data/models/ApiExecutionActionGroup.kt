package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

@kotlinx.serialization.Serializable
@Stable
@Immutable
data class ApiExecutionActionGroup(
    val label: String,
    val actions: List<Action>
) {
    @kotlinx.serialization.Serializable
    @Stable
    @Immutable
    data class Action(val deviceURL: String, val commands: List<Command>? = null, val execId: String? = null, val state: String? = null) {

        @kotlinx.serialization.Serializable(with = ApiCommandSerializer::class)
        @Stable
        @Immutable
        sealed class Command {
            abstract val name: String
            open val parameters: List<Any> = emptyList()

            @kotlinx.serialization.Serializable
            @Stable
            @Immutable
            data class EmptyCommand(override val name: String) : Command()

            @kotlinx.serialization.Serializable
            @Stable
            @Immutable
            data class StringCommand(override val name: String, override val parameters: List<String>) : Command()

            @kotlinx.serialization.Serializable
            @Stable
            @Immutable
            data class IntCommand(override val name: String, override val parameters: List<Int>) : Command()
        }

        object ApiCommandSerializer : JsonContentPolymorphicSerializer<Command>(Command::class) {
            override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Command> {
                val arrayElement = try {
                    element.jsonObject["parameters"]?.jsonArray?.first()
                } catch (error: Throwable) {
                    return Command.EmptyCommand.serializer()
                }
                return when {
                    arrayElement is JsonPrimitive && arrayElement.isString -> Command.StringCommand.serializer()
                    else -> Command.IntCommand.serializer()
                }
            }
        }
    }
}