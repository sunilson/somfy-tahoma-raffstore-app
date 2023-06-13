package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.data

import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.data.models.CurrentWeatherResponse
import retrofit2.http.GET

interface WeatherApi {

    @GET("current.json?key=ae3057198bc9414fa30194450232405&q=47.3609931,9.6507167&aqi=no")
    suspend fun getCurrentWeatherForStaudenstraße(): CurrentWeatherResponse

}