package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.di

import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.data.WeatherApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.UpsertScheduleUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.CheckIfSunnyUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.DeleteScheduleUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.ExecuteScheduleUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.GetScheduleUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.GetSchedulesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.domain.ObserveSchedulesToExecuteUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add.AddScheduleViewModel
import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview.SchedulesOverviewViewModel
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import retrofit2.Retrofit

val schedulesModule = module {
    viewModel { SchedulesOverviewViewModel(get()) }
    viewModel { AddScheduleViewModel(get(), get(), get(), get()) }
    factoryOf(::GetSchedulesUseCase)
    factoryOf(::UpsertScheduleUseCase)
    factoryOf(::DeleteScheduleUseCase)
    factoryOf(::ExecuteScheduleUseCase)
    factoryOf(::CheckIfSunnyUseCase)
    factoryOf(::GetScheduleUseCase)
    factoryOf(::ObserveSchedulesToExecuteUseCase)
    single {
        Retrofit
            .Builder()
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(
                        ChuckerInterceptor
                            .Builder(androidContext())
                            .collector(ChuckerCollector(androidContext(), showNotification = true))
                            .build()
                    )
                    .build()
            )
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(get())
            .build()
            .create(WeatherApi::class.java)
    }
}