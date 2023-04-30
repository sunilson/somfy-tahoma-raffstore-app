package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data

import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.models.ActivateTokenRequestBody
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.models.GenerateTokenResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TahomaAuthenticationApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("userId") userId: String,
        @Field("userPassword") userPassword: String
    ): Response<Unit>

    @GET("config/{pin}/local/tokens/generate")
    suspend fun createToken(@Path("pin") pin: String): GenerateTokenResponseBody

    @POST("config/{pin}/local/tokens")
    suspend fun activateToken(@Path("pin") pin: String, @Body body: ActivateTokenRequestBody)

    @GET("config/{pin}/local/tokens/devmode")
    suspend fun getTokens(@Path("pin") pin: String): Response<Unit>
}
