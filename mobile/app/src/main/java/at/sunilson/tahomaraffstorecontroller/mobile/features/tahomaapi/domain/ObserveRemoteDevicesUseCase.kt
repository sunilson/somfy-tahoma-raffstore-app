package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.getChildListFlow
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow

class ObserveRemoteDevicesUseCase(private val remoteDatabase: FirebaseDatabase) : FlowUseCase<Unit, List<Device>>() {
    override fun doWork(params: Unit): Flow<List<Device>> {
        return remoteDatabase.reference.child("devices").getChildListFlow()
    }
}