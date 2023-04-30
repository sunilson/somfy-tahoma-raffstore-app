package at.sunilson.tahomaraffstorecontroller.mobile.features.discovery.di

import android.content.Context
import android.net.nsd.NsdManager
import at.sunilson.tahomaraffstorecontroller.mobile.features.discovery.domain.DiscoverTahomaBoxUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.discovery.domain.ResolveTahomaBoxInformationUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val discoveryModule = module {
    factory { androidContext().getSystemService(Context.NSD_SERVICE) as NsdManager }
    factoryOf(::DiscoverTahomaBoxUseCase)
    factoryOf(::ResolveTahomaBoxInformationUseCase)
}