package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.di

import androidx.room.Room
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.TahomaAuthenticationInterceptor
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.TahomaAuthenticator
import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.ChangeUrlInterceptor
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ExecuteLocalApiAction
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ExecuteRemoteActionUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ListenToEventsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.LoadAndSyncDeviceUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.LoadAndSyncDevicesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ObserveRemoteActionsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ObserveRemoteDevicesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ObserveRemoteExecutionsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.RefreshExecutions
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.SaveTahomaBoxUrl
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.UpdateDeviceStatesUseCase
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.database.FirebaseDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

const val LOCAL_API_BASE_URL = "localApiBaseUrl"
const val LOCAL_API_HTTP_CLIENT = "localApiHttpClient"

val apiModule = module {
    factory { FirebaseDatabase.getInstance("https://tahoma-raffstore-controller-default-rtdb.europe-west1.firebasedatabase.app/") }
    factoryOf(::SaveTahomaBoxUrl)
    factoryOf(::ObserveRemoteActionsUseCase)
    factoryOf(::ExecuteRemoteActionUseCase)
    factoryOf(::LoadAndSyncDevicesUseCase)
    factoryOf(::LoadAndSyncDeviceUseCase)
    factoryOf(::ChangeUrlInterceptor)
    factoryOf(::ListenToEventsUseCase)
    factoryOf(::ExecuteLocalApiAction)
    factoryOf(::RefreshExecutions)
    factoryOf(::ObserveRemoteDevicesUseCase)
    factoryOf(::ObserveRemoteExecutionsUseCase)
    factoryOf(::UpdateDeviceStatesUseCase)
    single(named(LOCAL_API_BASE_URL)) { "https://replace.me/" }
    single(named(LOCAL_API_HTTP_CLIENT)) {
        val trustEverythingManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) =
                Unit

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) =
                Unit

            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        }

        val sslSocketFactory = SSLContext
            .getInstance("SSL")
            .apply { init(null, arrayOf(trustEverythingManager), SecureRandom()) }
            .socketFactory

        OkHttpClient
            .Builder()
            .sslSocketFactory(sslSocketFactory, trustEverythingManager)
            .hostnameVerifier { _, _ -> true }
            .addNetworkInterceptor(
                ChuckerInterceptor
                    .Builder(androidContext())
                    .collector(ChuckerCollector(androidContext(), showNotification = true))
                    .build()
            )
            .addInterceptor(get<ChangeUrlInterceptor>())
            .addInterceptor(get<TahomaAuthenticationInterceptor>())
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .authenticator(get<TahomaAuthenticator>())
            .build()
    }
    single<TahomaLocalApi> {
        Retrofit
            .Builder()
            .client(get(named(LOCAL_API_HTTP_CLIENT)))
            .baseUrl(get<String>(named(LOCAL_API_BASE_URL)))
            .addConverterFactory(get())
            .build()
            .create(TahomaLocalApi::class.java)
    }
    single {
        Room.databaseBuilder(
            androidApplication(),
            TahomaLocalDatabase::class.java,
            "tahoma-local-database"
        ).fallbackToDestructiveMigration().build()
    }
}