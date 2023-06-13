package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.ActionToExecute
import at.sunilson.tahomaraffstorecontroller.mobile.shared.data.FirebaseChildEvent
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getChildEventsFlow
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ObserveRemoteActionsUseCase(private val remoteDatabase: FirebaseDatabase) : FlowUseCase<Unit, Pair<String, ActionToExecute>>() {
    override fun doWork(params: Unit): Flow<Pair<String, ActionToExecute>> {
        return remoteDatabase.reference
            .child("actions")
            .getChildEventsFlow<String>()
            .filterIsInstance<FirebaseChildEvent.Added<String>>()
            .map { it.key to Json.decodeFromString(it.value) }
    }
}