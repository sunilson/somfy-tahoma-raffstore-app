package at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.di

import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.domain.FavouriteDeviceUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.domain.GetFavouriteDeviceIdsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.presentation.overview.RaffstoresOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val overviewModule = module {
    factoryOf(::GetFavouriteDeviceIdsUseCase)
    factoryOf(::FavouriteDeviceUseCase)
    viewModelOf(::RaffstoresOverviewViewModel)
}