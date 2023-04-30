package at.sunilson.tahomaraffstorecontroller.mobile.features.deeplink.di

import at.sunilson.tahomaraffstorecontroller.mobile.features.deeplink.domain.HandleDeeplinksUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val deepLinkModule = module {
    factoryOf(::HandleDeeplinksUseCase)
}