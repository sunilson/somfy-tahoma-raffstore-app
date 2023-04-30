package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import timber.log.Timber

class LoadDevicesUseCase(
    private val tahomaLocalApi: TahomaLocalApi,
    private val tahomaLocalDatabase: TahomaLocalDatabase
) : UseCase<Unit, Unit>() {
    override suspend fun doWork(params: Unit) {
        Timber.d("Loading devices")
        val newDevices = tahomaLocalApi.getDevices()
            .filter { it.states.any { it.name == "core:SlateOrientationState" } }
            .map(::Device)
        val currentDevices = tahomaLocalDatabase.dao().getAllDevicesOnce()
        tahomaLocalDatabase.dao().upsertDevices(newDevices)
        val devicesToDelete = currentDevices.filter { currentDevice -> newDevices.none { currentDevice.id == it.id } }
        tahomaLocalDatabase.dao().deleteDevices(devicesToDelete)
    }
}