package at.sunilson.tahomaraffstorecontroller.mobile.features.widget.presentation

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.koin.core.component.KoinComponent

object FavouritesWidgetUtils : KoinComponent {

    suspend fun updateState(context: Context, glanceId: GlanceId, block: MutablePreferences.() -> Unit) {
        updateAppWidgetState(context, glanceId) { it.block() }
        FavouritesWidget().update(context, glanceId)
    }

    fun enqueueWidgetUpdate(context: Context = getKoin().get()) {
        WorkManager.getInstance(context).enqueue(OneTimeWorkRequestBuilder<FavouritesWidgetWorker>().build())
    }

}