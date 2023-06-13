package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetFavouriteGroups(
    private val tahomaLocalDatabase: TahomaLocalDatabase,
    private val getActionGroupsUseCase: GetActionGroupsUseCase
) : FlowUseCase<Unit, List<ExecutionActionGroup>>() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun doWork(params: Unit): Flow<List<ExecutionActionGroup>> {
        return tahomaLocalDatabase.dao().getFavouriteGroups().flatMapLatest { favouriteGroups ->
            getActionGroupsUseCase(Unit).map { groups ->
                groups.filter { group -> favouriteGroups.any { it.id == group.id } }
            }
        }
    }
}