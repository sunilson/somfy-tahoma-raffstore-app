package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.di

import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.DeleteActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.FavouriteGroupUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetActionGroupsUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetFavouriteGroups
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.UpsertActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.add.AddGroupViewModel
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.overview.GroupsOverviewViewModel
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ExecuteActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.UpdateShortcuts
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val groupsModule = module {
    factoryOf(::GetFavouriteGroups)
    factoryOf(::FavouriteGroupUseCase)
    factoryOf(::UpdateShortcuts)
    factoryOf(::DeleteActionGroup)
    factoryOf(::ExecuteActionGroup)
    factoryOf(::GetActionGroupsUseCase)
    factoryOf(::UpsertActionGroup)
    factoryOf(::GetActionGroup)
    viewModelOf(::GroupsOverviewViewModel)
    viewModelOf(::AddGroupViewModel)
}