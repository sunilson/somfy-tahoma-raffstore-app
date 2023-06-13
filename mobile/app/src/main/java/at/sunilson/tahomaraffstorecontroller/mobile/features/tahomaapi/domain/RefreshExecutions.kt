package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.models.LocalApiExecution
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getListSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.removeValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.setValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase

class RefreshExecutions(
    private val tahomaLocalApi: TahomaLocalApi,
    private val remoteDatabase: FirebaseDatabase
) : UseCase<Unit, List<LocalApiExecution>>() {
    override suspend fun doWork(params: Unit): List<LocalApiExecution> {
        val executions = tahomaLocalApi.getAllExecutions()
        val remoteExecutions = remoteDatabase.reference.child("executions").getListSuspending<Execution>()
        val toRemove = remoteExecutions.filter { remote -> executions.none { it.id == remote.id } }
        toRemove.forEach { remoteDatabase.reference.child("executions").child(it.id).removeValueSuspending() }
        executions.forEach { remoteDatabase.reference.child("executions").child(it.id).setValueSuspending(it) }
        return executions
    }
}