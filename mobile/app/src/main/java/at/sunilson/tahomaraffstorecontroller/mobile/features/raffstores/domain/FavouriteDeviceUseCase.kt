package at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.UpdateShortcuts
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.data.models.FavouriteDevice
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.presentation.FavouritesWidgetUtils
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class FavouriteDeviceUseCase(
    private val tahomaLocalDatabase: TahomaLocalDatabase,
    private val updateShortcuts: UpdateShortcuts
) : UseCase<String, Unit>() {

    override suspend fun doWork(params: String) {
        val favouriteDevice = tahomaLocalDatabase.dao().getFavouriteDeviceOnce(params)
        if (favouriteDevice == null) {
            tahomaLocalDatabase.dao().upsertFavouriteDevice(FavouriteDevice(params))
        } else {
            tahomaLocalDatabase.dao().deleteFavouriteDevice(params)
        }
        updateShortcuts(Unit)
        FavouritesWidgetUtils.enqueueWidgetUpdate()
    }
}