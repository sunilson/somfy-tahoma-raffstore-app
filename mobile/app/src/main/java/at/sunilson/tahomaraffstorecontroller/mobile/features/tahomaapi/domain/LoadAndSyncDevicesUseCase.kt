package at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.sanitizeFirebaseId
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FirebaseUtils.setValueSuspending
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import com.google.firebase.database.FirebaseDatabase

class LoadAndSyncDevicesUseCase(
    private val tahomaLocalApi: TahomaLocalApi,
    private val firebaseDatabase: FirebaseDatabase
) : UseCase<Unit, List<Device>>() {
    override suspend fun doWork(params: Unit): List<Device> {
        val devices = tahomaLocalApi.getDevices()
            .filter { it.states.any { it.name == "core:SlateOrientationState" } }
            .map(::Device)

        devices.forEach { device ->
            firebaseDatabase.reference
                .child("devices")
                .child(device.id.sanitizeFirebaseId())
                .setValueSuspending(device)
        }
        return devices
    }
}