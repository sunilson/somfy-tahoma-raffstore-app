package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class GetActionGroup(private val tahomaLocalDatabase: TahomaLocalDatabase) : UseCase<String, LocalExecutionActionGroup>() {
    override suspend fun doWork(params: String): LocalExecutionActionGroup {
        return tahomaLocalDatabase.dao().getExecutionActionGroupOnce(params) ?: error("Action group with id $params not found")
    }
}