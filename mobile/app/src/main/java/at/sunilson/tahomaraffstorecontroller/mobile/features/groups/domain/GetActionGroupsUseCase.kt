package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getChildListFlow
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow

class GetActionGroupsUseCase(private val firebaseDatabase: FirebaseDatabase) : FlowUseCase<Unit, List<ExecutionActionGroup>>() {
    override fun doWork(params: Unit): Flow<List<ExecutionActionGroup>> {
        return firebaseDatabase.reference
            .child("groups")
            .getChildListFlow<ExecutionActionGroup>()
    }
}