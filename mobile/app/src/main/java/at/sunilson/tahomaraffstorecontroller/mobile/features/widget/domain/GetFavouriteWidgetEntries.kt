package at.sunilson.tahomaraffstorecontroller.mobile.features.widget.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.QUERY_EXECUTE_ACTION_GROUP_ID
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.QUERY_EXECUTE_DEVICE_URL
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain.GetFavouriteGroups
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.domain.GetFavouriteDevices
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.domain.entities.FavouriteWidgetEntries
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.domain.entities.FavouriteWidgetEntry
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class GetFavouriteWidgetEntries(
    private val getFavouriteDevices: GetFavouriteDevices,
    private val getFavouriteGroups: GetFavouriteGroups
) : UseCase<Unit, FavouriteWidgetEntries>() {
    override suspend fun doWork(params: Unit): FavouriteWidgetEntries {
        return coroutineScope {
            val jobs = listOf(
                async {
                    getFavouriteDevices(Unit).first().map {
                        FavouriteWidgetEntry(
                            id = it.id.hashCode().toLong(),
                            label = it.name,
                            deeplink = "sunilson://tahoma/action/execute/device/my?$QUERY_EXECUTE_DEVICE_URL=${it.id}"
                        )
                    }
                },
                async {
                    getFavouriteGroups(Unit).first().map {
                        FavouriteWidgetEntry(
                            id = it.id.hashCode().toLong(),
                            label = it.label,
                            deeplink = "sunilson://tahoma/action/execute/group?$QUERY_EXECUTE_ACTION_GROUP_ID=${it.id}"
                        )
                    }
                },
            )

            FavouriteWidgetEntries(jobs.awaitAll().flatten())
        }
    }
}