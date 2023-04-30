package at.sunilson.tahomaraffstorecontroller.mobile.features.widget.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.domain.entities.FavouriteWidgetEntries
import at.sunilson.tahomaraffstorecontroller.mobile.main.MainActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// Tutorial https://www.droidcon.com/2022/11/15/take-a-look-at-jetpack-glance/
// If we would like a configuration activity https://github.com/google/glance-experimental-tools/tree/main/appwidget-configuration
class FavouritesWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    @Composable
    override fun Content() {
        val preferences = currentState<Preferences>()
        val entriesString = preferences[stringPreferencesKey(KEY_WIDGET_ENTRIES)]
        val entries = entriesString?.let { Json.decodeFromString<FavouriteWidgetEntries>(it) }

        if (entries == null) {
            Button(text = "Load data", onClick = actionRunCallback<FavouritesWidgetRefreshAction>())
            return
        }

        Box(
            modifier = GlanceModifier.appWidgetBackground()
                .clickable(actionRunCallback<OpenAppAction>())
                .padding(vertical = 10.dp)
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn {
                entries.entries.forEachIndexed { index, entry ->
                    item(itemId = entry.id) {
                        Column {
                            FavouritesWidgetItem(entry)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val KEY_WIDGET_ENTRIES = "widgetEntries"
    }

}

class OpenAppAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        context.startActivity(Intent(context, MainActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        })
    }
}

class FavouritesWidgetRefreshAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        WorkManager.getInstance(context).enqueue(OneTimeWorkRequestBuilder<FavouritesWidgetWorker>().build())
    }
}