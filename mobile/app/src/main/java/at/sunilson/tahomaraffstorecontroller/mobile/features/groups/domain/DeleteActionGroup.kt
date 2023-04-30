package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class DeleteActionGroup(private val tahomaLocalDatabase: TahomaLocalDatabase) : UseCase<String, Unit>() {
    override suspend fun doWork(params: String) {
        tahomaLocalDatabase.dao().deleteExecutionActionGroup(params)
    }
}