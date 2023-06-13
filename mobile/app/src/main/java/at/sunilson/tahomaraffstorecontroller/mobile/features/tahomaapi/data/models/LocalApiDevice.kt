package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models

import androidx.compose.runtime.Immutable
import androidx.room.PrimaryKey
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

@Serializable
data class LocalApiDevice(
    @PrimaryKey
    val deviceURL: String,
    val label: String,
    val available: Boolean,
    val synced: Boolean,
    val type: Int,
    val states: List<State>
) {

    val status: String
        get() = states
            .filterIsInstance<State.StringState>()
            .firstOrNull { it.name == "core:StatusState" }
            ?.value.orEmpty()

    @Serializable(with = ApiDeviceStateSerializer::class)
    @Immutable
    sealed class State {
        abstract val type: Int
        abstract val name: String
        open val value: Any = Unit

        @Serializable
        @Immutable
        data class StringState(
            override val type: Int,
            override val name: String,
            override val value: String
        ) : State()

        @Serializable
        @Immutable
        data class IntState(
            override val type: Int,
            override val name: String,
            override val value: Int
        ) : State()

        @Serializable
        @Immutable
        data class BooleanState(
            override val type: Int,
            override val name: String,
            override val value: Boolean
        ) : State()

        @Serializable
        @Immutable
        data class OtherState(
            override val type: Int,
            override val name: String
        ) : State()
    }

    object ApiDeviceStateSerializer : JsonContentPolymorphicSerializer<State>(State::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out State> {
            val element = element.jsonObject["value"]
            return if (element is JsonPrimitive) {
                when {
                    element.isString -> State.StringState.serializer()
                    element.content.toBooleanStrictOrNull() != null -> State.BooleanState.serializer()
                    else -> State.IntState.serializer()
                }
            } else {
                State.OtherState.serializer()
            }
        }
    }
}
