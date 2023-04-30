package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class UpdateActionGroup(private val tahomaLocalDatabase: TahomaLocalDatabase) : UseCase<LocalExecutionActionGroup, Unit>() {
    override suspend fun doWork(params: LocalExecutionActionGroup) {
        tahomaLocalDatabase.dao().upsertExecutionActionGroup(params)
    }
}