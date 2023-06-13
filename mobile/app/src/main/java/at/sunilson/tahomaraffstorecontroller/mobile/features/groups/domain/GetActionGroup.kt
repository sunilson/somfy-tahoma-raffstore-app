package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase

class GetActionGroup(private val firebaseDatabase: FirebaseDatabase) : UseCase<String, ExecutionActionGroup>() {
    override suspend fun doWork(params: String): ExecutionActionGroup {
        return firebaseDatabase.reference
            .child("groups/$params")
            .getSuspending<ExecutionActionGroup>() ?: error("No action group found")
    }
}