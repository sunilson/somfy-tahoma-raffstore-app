package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import java.util.UUID

class CreateActionGroup(private val tahomaLocalDatabase: TahomaLocalDatabase) : UseCase<CreateActionGroup.Params, Unit>() {
    data class Params(val label: String, val devicesInTargetState: List<Device>, val id: String? = null)

    override suspend fun doWork(params: Params) {
        val id = params.id ?: UUID.randomUUID().toString()
        tahomaLocalDatabase.dao().upsertExecutionActionGroup(LocalExecutionActionGroup(id, params.label, params.devicesInTargetState))
    }
}