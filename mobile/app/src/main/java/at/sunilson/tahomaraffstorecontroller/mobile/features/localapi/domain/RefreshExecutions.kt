package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class RefreshExecutions(
    private val tahomaLocalApi: TahomaLocalApi,
    private val tahomaLocalDatabase: TahomaLocalDatabase
) : UseCase<Unit, List<Execution>>() {
    override suspend fun doWork(params: Unit): List<Execution> {
        val executions = tahomaLocalApi.getAllExecutions()
        tahomaLocalDatabase.dao().deleteAllExecutions()
        tahomaLocalDatabase.dao().upsertExecutions(executions)
        return executions
    }
}