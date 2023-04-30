package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetActionGroups(private val tahomaLocalDatabase: TahomaLocalDatabase) : FlowUseCase<Unit, List<LocalExecutionActionGroup>>() {
    override fun doWork(params: Unit): Flow<List<LocalExecutionActionGroup>> {
        return tahomaLocalDatabase.dao().getAllExecutionActionGroups()
    }
}