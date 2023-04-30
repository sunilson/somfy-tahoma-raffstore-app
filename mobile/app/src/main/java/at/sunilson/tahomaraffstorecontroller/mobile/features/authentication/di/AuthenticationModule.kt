package at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.di

import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.SessionCookieInterceptor
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.TahomaAuthenticationApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.TahomaAuthenticationInterceptor
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.TahomaAuthenticator
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.CheckUserCredentialsExistingUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.GetUserCredentialsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.LoginUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.LogoutUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.domain.StoreUserCredentialsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.presentation.login.LoginViewModel
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit


const val AUTHENTICATION_BASE_URL = "authenticationBaseUrl"
const val AUTHENTICATION_HTTP_CLIENT = "authenticationHttpClient"

private val authenticationViewModels = module {
    viewModelOf(::LoginViewModel)
}

private val authenticationFactories = module {
    factoryOf(::LoginUseCase)
    factoryOf(::StoreUserCredentialsUseCase)
    factoryOf(::GetUserCredentialsUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::CheckUserCredentialsExistingUseCase)
    factoryOf(::SessionCookieInterceptor)
    factoryOf(::TahomaAuthenticationInterceptor)
    factoryOf(::TahomaAuthenticator)
}

private val authenticationSingles = module {
    single(named(AUTHENTICATION_BASE_URL)) { "https://ha101-1.overkiz.com/enduser-mobile-web/enduserAPI/" }
    single(named(AUTHENTICATION_HTTP_CLIENT)) {
        OkHttpClient
            .Builder()
            .addNetworkInterceptor(
                ChuckerInterceptor
                    .Builder(androidContext())
                    .collector(ChuckerCollector(androidContext(), showNotification = true))
                    .build()
            )
            .addInterceptor(get<SessionCookieInterceptor>())
            .addInterceptor(HttpLoggingInterceptor().apply { level = BODY })
            .build()
    }
    single<TahomaAuthenticationApi> {
        Retrofit
            .Builder()
            .client(get(named(AUTHENTICATION_HTTP_CLIENT)))
            .baseUrl(get<String>(named(AUTHENTICATION_BASE_URL)))
            .addConverterFactory(get())
            .build()
            .create(TahomaAuthenticationApi::class.java)
    }
}

val authenticationModule = module {
    includes(authenticationFactories, authenticationSingles, authenticationViewModels)
}