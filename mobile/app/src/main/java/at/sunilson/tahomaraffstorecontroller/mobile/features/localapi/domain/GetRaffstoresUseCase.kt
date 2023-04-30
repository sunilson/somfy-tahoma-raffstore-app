package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRaffstoresUseCase(private val localDatabase: TahomaLocalDatabase) : FlowUseCase<Unit, List<Device>>() {

    override fun doWork(params: Unit): Flow<List<Device>> {
        return localDatabase
            .dao()
            .getAllDevices()
            .map { devices ->
                devices
                    .filter { device -> device.available }
                    .sortedBy { it.id }
            }
    }

}