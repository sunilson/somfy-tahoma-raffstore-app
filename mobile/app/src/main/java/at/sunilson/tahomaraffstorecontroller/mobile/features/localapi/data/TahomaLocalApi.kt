package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data

import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.ApiDevice
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.ApiExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Event
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.EventListenerRegistrationResponse
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.ExecutionResponse
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
    suspend fun getDevices(): List<ApiDevice>

    @GET("setup/devices/{deviceUrl}")
    suspend fun getDevice(@Path("deviceUrl") deviceUrl: String): ApiDevice

    @GET("setup/devices/{deviceUrl}/states")
    suspend fun getDeviceStates(@Path("deviceUrl") deviceUrl: String): List<ApiDevice.State>

    @POST("events/register")
    suspend fun registerEventListener(): EventListenerRegistrationResponse

    @POST("events/{listenerId}/fetch")
    suspend fun fetchNewEvents(@Path("listenerId") listenerId: String): List<Event>

    @POST("events/{listenerId}/unregister")
    suspend fun unRegisterEventListener(@Path("listenerId") listenerId: String)

    @POST("exec/apply")
    @Headers("Content-Type: application/json", "Accept: */*")
    suspend fun executeCommands(@Body apiExecutionActionGroup: ApiExecutionActionGroup): ExecutionResponse

    @GET("exec/current")
    suspend fun getAllExecutions(): List<Execution>

    @DELETE("exec/current/setup")
    suspend fun stopAllExecutions()

    @DELETE("exec/current/setup/{executionId}")
    suspend fun stopExecution(@Path("executionId") executionId: String)
}