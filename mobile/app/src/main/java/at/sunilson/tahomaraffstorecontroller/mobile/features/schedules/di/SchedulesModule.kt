package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.di

import at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview.SchedulesOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val schedulesModule = module {
    viewModel { SchedulesOverviewViewModel() }
}