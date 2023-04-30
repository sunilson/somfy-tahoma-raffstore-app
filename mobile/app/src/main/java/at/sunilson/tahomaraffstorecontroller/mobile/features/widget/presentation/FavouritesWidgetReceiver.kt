package at.sunilson.tahomaraffstorecontroller.mobile.features.widget.presentation

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class FavouritesWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FavouritesWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        FavouritesWidgetUtils.enqueueWidgetUpdate(context)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        FavouritesWidgetUtils.enqueueWidgetUpdate(context)
    }
}