package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerialName("current")
    val current: Current,
    @SerialName("location")
    val location: Location
)