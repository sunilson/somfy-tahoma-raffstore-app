package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.setValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class UpsertActionGroup(private val firebaseDatabase: FirebaseDatabase) : UseCase<UpsertActionGroup.Params, Unit>() {
    data class Params(val label: String, val devicesInTargetState: List<Device>, val id: String? = null)

    override suspend fun doWork(params: Params) {
        val id = params.id ?: UUID.randomUUID().toString()
        firebaseDatabase.reference
            .child("groups/$id")
            .setValueSuspending(ExecutionActionGroup(id, params.label, params.devicesInTargetState))
    }
}