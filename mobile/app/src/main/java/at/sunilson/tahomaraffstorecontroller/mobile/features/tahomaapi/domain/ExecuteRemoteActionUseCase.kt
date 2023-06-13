package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.ActionToExecute
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ExecuteRemoteActionUseCase(private val remoteDatabase: FirebaseDatabase) : UseCase<ActionToExecute, Unit>() {
    override suspend fun doWork(params: ActionToExecute) {
        remoteDatabase.reference
            .child("actions")
            .push()
            .setValue(Json.encodeToString(params))
    }
}