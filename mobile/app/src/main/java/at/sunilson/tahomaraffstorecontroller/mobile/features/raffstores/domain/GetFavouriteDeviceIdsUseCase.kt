package at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.domain

import at.sunilson.tahomaraffstorecontroller.mobile.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.tahomaapi.domain.ObserveRemoteDevicesUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetFavouriteDeviceIdsUseCase(
    private val tahomaLocalDatabase: TahomaLocalDatabase,
    private val observeRemoteDevicesUseCase: ObserveRemoteDevicesUseCase
) : FlowUseCase<Unit, List<String>>() {
    override fun doWork(params: Unit): Flow<List<String>> {
        return tahomaLocalDatabase.dao().getFavouriteDevices().map { it.map { it.id } }
    }
}