package at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.GetRaffstoresUseCase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetFavouriteDevices(
    private val tahomaLocalDatabase: TahomaLocalDatabase,
    private val getRaffstoresUseCase: GetRaffstoresUseCase
) : FlowUseCase<Unit, List<Device>>() {
    override fun doWork(params: Unit): Flow<List<Device>> {
        return tahomaLocalDatabase.dao().getFavouriteDevices().flatMapLatest { favouriteUrls ->
            getRaffstoresUseCase(Unit).map { devices ->
                devices.filter { device ->
                    favouriteUrls.toSet().any { it.id == device.id }
                }
            }
        }
    }
}