package at.sunilson.tahomaraffstorecontroller.mobile.features.widget.presentation

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.domain.GetFavouriteWidgetEntries
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.domain.entities.FavouriteWidgetEntries
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FavouritesWidgetWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val getFavouriteWidgetEntries: GetFavouriteWidgetEntries,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val widgetEntries: FavouriteWidgetEntries = getFavouriteWidgetEntries(Unit).getOrNull() ?: return Result.failure()
        val glanceIds: List<GlanceId> = GlanceAppWidgetManager(context).getGlanceIds(FavouritesWidget::class.java)
        if (glanceIds.isEmpty()) return Result.failure()

        glanceIds.forEach { glanceId ->
            FavouritesWidgetUtils.updateState(context, glanceId) {
                this[stringPreferencesKey(FavouritesWidget.KEY_WIDGET_ENTRIES)] = Json.encodeToString(widgetEntries)
            }
        }

        return Result.success()
    }

    companion object {
        const val TAG = "FavouritesWidgetWorker"
    }
}