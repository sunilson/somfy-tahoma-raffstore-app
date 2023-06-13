package at.sunilson.tahomaraffstorecontroller.mobile.features.widget.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.ActionParameters.Key
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.Text
import at.sunilson.tahomaraffstorecontroller.mobile.R
import at.sunilson.tahomaraffstorecontroller.mobile.features.widget.domain.entities.FavouriteWidgetEntry
import timber.log.Timber

@Composable
fun FavouritesWidgetItem(entry: FavouriteWidgetEntry) {
    Row(
        modifier = GlanceModifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable(actionRunCallback<FavouritesWidgetItemAction>(actionParametersOf(Key<FavouriteWidgetEntry>("entry") to entry)))
            .padding(20.dp)
    ) {
        Text(
            text = entry.label,
            modifier = GlanceModifier.defaultWeight()
        )
        Image(
            provider = ImageProvider(R.drawable.ic_play_circle),
            modifier = GlanceModifier.size(24.dp),
            contentDescription = ""
        )
    }
}

class FavouritesWidgetItemAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val entry = parameters.get<FavouriteWidgetEntry>(Key("entry")) ?: return
        Timber.d("Clicked favourites widget with id $glanceId: ${entry.deeplink}")
        val executeActionIntent = Intent().apply {
            component = ComponentName(
                context.packageName,
                "at.sunilson.tahomaraffstorecontroller.mobile.features.widget.FavouriteWidgetClickBroadcastReceiver"
            )
            data = Uri.parse(entry.deeplink)
        }
        context.sendBroadcast(executeActionIntent)
    }
}