package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.domain

import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.data.models.FavouriteGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.UpdateShortcuts
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.presentation.FavouritesWidgetUtils
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase

class FavouriteGroupUseCase(
    private val tahomaLocalDatabase: TahomaLocalDatabase,
    private val updateShortcuts: UpdateShortcuts
) : UseCase<String, Unit>() {

    override suspend fun doWork(params: String) {
        val favouriteGroup = tahomaLocalDatabase.dao().getFavouriteGroup(params)
        if (favouriteGroup == null) {
            tahomaLocalDatabase.dao().upsertFavouriteExecutionActionGroup(FavouriteGroup(params))
        } else {
            tahomaLocalDatabase.dao().deleteFavouriteExecutionActionGroup(params)
        }
        updateShortcuts(Unit)
        FavouritesWidgetUtils.enqueueWidgetUpdate()
    }
}