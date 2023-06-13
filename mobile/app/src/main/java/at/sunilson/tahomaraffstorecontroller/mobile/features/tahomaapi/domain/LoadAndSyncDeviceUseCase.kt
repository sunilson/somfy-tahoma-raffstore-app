package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.sanitizeFirebaseId
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.setValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase

class LoadAndSyncDeviceUseCase(
    private val tahomaLocalApi: TahomaLocalApi,
    private val firebaseDatabase: FirebaseDatabase
) : UseCase<String, Device>() {
    override suspend fun doWork(params: String): Device {
        val device = Device(tahomaLocalApi.getDevice(params))
        firebaseDatabase.reference
            .child("devices")
            .child(device.id.sanitizeFirebaseId())
            .setValueSuspending(device)
        return device
    }
}