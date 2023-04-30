package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetExecutions(private val localDatabase: TahomaLocalDatabase) : FlowUseCase<Unit, List<Execution>>() {
    override fun doWork(params: Unit): Flow<List<Execution>> {
        return localDatabase.dao().getAllExecutions()
    }
}