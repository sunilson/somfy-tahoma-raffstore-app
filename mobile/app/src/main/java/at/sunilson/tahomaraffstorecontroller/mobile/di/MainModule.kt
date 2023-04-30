package at.sunilson.tahomaraffstorecontroller.mobile.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.di.authenticationModule
import at.sunilson.tahomaraffstorecontroller.mobile.features.deeplink.di.deepLinkModule
import at.sunilson.tahomaraffstorecontroller.mobile.features.discovery.di.discoveryModule
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.di.localApiModule
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.di.overviewModule
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.di.schedulesModule
import at.sunilson.tahomaraffstorecontroller.mobile.main.MainViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
val mainModule = module {
    includes(localApiModule)
    includes(authenticationModule)
    includes(discoveryModule)
    includes(overviewModule)
    includes(deepLinkModule)
    includes(schedulesModule)
    factory { get<Json>().asConverterFactory("application/json".toMediaType()) }
    viewModelOf(::MainViewModel)
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single {
        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            "sharedPrefs",
            masterKeyAlias,
            androidContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }
    single { androidContext().getSharedPreferences("sharedPrefsUnencrypted", Context.MODE_PRIVATE) }
}