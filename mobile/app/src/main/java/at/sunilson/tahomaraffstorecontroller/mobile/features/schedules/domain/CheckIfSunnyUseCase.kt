package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.data.WeatherApi
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class CheckIfSunnyUseCase(private val weatherApi: WeatherApi) : UseCase<Unit, Boolean>() {

    override suspend fun doWork(params: Unit): Boolean {
        val condition = weatherApi.getCurrentWeatherForStaudenstraße().current.condition.text
        return condition.equals("sunny", ignoreCase = true) || condition.equals("partly cloudy", ignoreCase = true)
    }

    /**
     * code	day	night
     * 1000	Sunny	Clear
     * 1003	Partly cloudy	Partly cloudy
     * 1006	Cloudy	Cloudy
     * 1009	Overcast	Overcast
     * 1030	Mist	Mist
     * 1063	Patchy rain possible	Patchy rain possible
     * 1066	Patchy snow possible	Patchy snow possible
     * 1069	Patchy sleet possible	Patchy sleet possible
     * 1072	Patchy freezing drizzle possible	Patchy freezing drizzle possible
     * 1087	Thundery outbreaks possible	Thundery outbreaks possible
     * 1114	Blowing snow	Blowing snow
     * 1117	Blizzard	Blizzard
     * 1135	Fog	Fog
     * 1147	Freezing fog	Freezing fog
     * 1150	Patchy light drizzle	Patchy light drizzle
     * 1153	Light drizzle	Light drizzle
     * 1168	Freezing drizzle	Freezing drizzle
     * 1171	Heavy freezing drizzle	Heavy freezing drizzle
     * 1180	Patchy light rain	Patchy light rain
     * 1183	Light rain	Light rain
     * 1186	Moderate rain at times	Moderate rain at times
     * 1189	Moderate rain	Moderate rain
     * 1192	Heavy rain at times	Heavy rain at times
     * 1195	Heavy rain	Heavy rain
     * 1198	Light freezing rain	Light freezing rain
     * 1201	Moderate or heavy freezing rain	Moderate or heavy freezing rain
     * 1204	Light sleet	Light sleet
     * 1207	Moderate or heavy sleet	Moderate or heavy sleet
     * 1210	Patchy light snow	Patchy light snow
     * 1213	Light snow	Light snow
     * 1216	Patchy moderate snow	Patchy moderate snow
     * 1219	Moderate snow	Moderate snow
     * 1222	Patchy heavy snow	Patchy heavy snow
     * 1225	Heavy snow	Heavy snow
     * 1237	Ice pellets	Ice pellets
     * 1240	Light rain shower	Light rain shower
     * 1243	Moderate or heavy rain shower	Moderate or heavy rain shower
     * 1246	Torrential rain shower	Torrential rain shower
     * 1249	Light sleet showers	Light sleet showers
     * 1252	Moderate or heavy sleet showers	Moderate or heavy sleet showers
     * 1255	Light snow showers	Light snow showers
     * 1258	Moderate or heavy snow showers	Moderate or heavy snow showers
     * 1261	Light showers of ice pellets	Light showers of ice pellets
     * 1264	Moderate or heavy showers of ice pellets	Moderate or heavy showers of ice pellets
     * 1273	Patchy light rain with thunder	Patchy light rain with thunder
     * 1276	Moderate or heavy rain with thunder	Moderate or heavy rain with thunder
     * 1279	Patchy light snow with thunder	Patchy light snow with thunder
     * 1282	Moderate or heavy snow with thunder	Moderate or heavy snow with thunder
     */

}