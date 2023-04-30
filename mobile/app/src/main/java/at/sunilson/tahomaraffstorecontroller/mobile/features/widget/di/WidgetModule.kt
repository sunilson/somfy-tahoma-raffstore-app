package at.sunilson.tahomaraffstorecontroller.mobile.features.widget.di

import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.domain.GetFavouriteWidgetEntries
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.presentation.FavouritesWidgetWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.dsl.module

val widgetModule = module {
    factoryOf(::GetFavouriteWidgetEntries)
    workerOf(::FavouritesWidgetWorker) { named<FavouritesWidgetWorker>() }
}