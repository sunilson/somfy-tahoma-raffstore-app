package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getChildListFlow
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow

class ObserveRemoteExecutionsUseCase(private val remoteDatabase: FirebaseDatabase) : FlowUseCase<Unit, List<Execution>>() {
    override fun doWork(params: Unit): Flow<List<Execution>> {
        return remoteDatabase.reference.child("executions").getChildListFlow()
    }
}