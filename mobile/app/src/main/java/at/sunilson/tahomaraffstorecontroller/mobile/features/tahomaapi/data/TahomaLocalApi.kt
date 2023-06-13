package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data

import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiDevice
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiEvent
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiEventListenerRegistrationResponse
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecution
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecutionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface TahomaLocalApi {

    @GET("apiVersion")
    suspend fun getApiVersion(): Response<Unit>

    @GET("setup/devices")
    suspend fun getDevices(): List<LocalApiDevice>

    @GET("setup/devices/{deviceUrl}")
    suspend fun getDevice(@Path("deviceUrl") deviceUrl: String): LocalApiDevice

    @GET("setup/devices/{deviceUrl}/states")
    suspend fun getDeviceStates(@Path("deviceUrl") deviceUrl: String): List<LocalApiDevice.State>

    @POST("events/register")
    suspend fun registerEventListener(): LocalApiEventListenerRegistrationResponse

    @POST("events/{listenerId}/fetch")
    suspend fun fetchNewEvents(@Path("listenerId") listenerId: String): List<LocalApiEvent>

    @POST("events/{listenerId}/unregister")
    suspend fun unRegisterEventListener(@Path("listenerId") listenerId: String)

    @POST("exec/apply")
    @Headers("Content-Type: application/json", "Accept: */*")
    suspend fun executeCommands(@Body localApiExecutionActionGroup: LocalApiExecutionActionGroup): LocalApiExecutionResponse

    @GET("exec/current")
    suspend fun getAllExecutions(): List<LocalApiExecution>

    @DELETE("exec/current/setup")
    suspend fun stopAllExecutions()

    @DELETE("exec/current/setup/{executionId}")
    suspend fun stopExecution(@Path("executionId") executionId: String)
}