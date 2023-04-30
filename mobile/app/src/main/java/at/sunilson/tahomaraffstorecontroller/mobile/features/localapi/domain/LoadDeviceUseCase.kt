package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.TahomaLocalApi
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import timber.log.Timber

class LoadDeviceUseCase(
    private val tahomaLocalApi: TahomaLocalApi,
    private val tahomaLocalDatabase: TahomaLocalDatabase
) : UseCase<String, Unit>() {
    override suspend fun doWork(params: String) {
        val device = tahomaLocalApi.getDevice(params)
        Timber.d("Loaded device ${device.label}")
        tahomaLocalDatabase.dao().upsertDevice(Device(device))
    }
}