package at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import at.sunilson.tahomaraffstorecontroller.mobile.R
import at.sunilson.tahomaraffstorecontroller.mobile.features.authentication.data.QUERY_EXECUTE_ACTION_GROUP_ID
import at.sunilson.tahomaraffstorecontroller.mobile.features.database.TahomaLocalDatabase
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.domain.UseCase
import kotlinx.coroutines.flow.first

class UpdateShortcuts(private val context: Context, private val localDatabase: TahomaLocalDatabase) : UseCase<Unit, Unit>() {
    override suspend fun doWork(params: Unit) {
        val favouriteGroups = localDatabase.dao()
            .getFavouriteGroups()
            .first()
            .mapNotNull { localDatabase.dao().getExecutionActionGroupOnce(it.id) }

        ShortcutManagerCompat.removeAllDynamicShortcuts(context)
        ShortcutManagerCompat.addDynamicShortcuts(context, createGroupShortcuts(favouriteGroups))
    }

    private fun createGroupShortcuts(groups: List<LocalExecutionActionGroup>): List<ShortcutInfoCompat> {
        return groups.map { actionGroup ->
            val groupIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("sunilson://tahoma/action/execute/group?$QUERY_EXECUTE_ACTION_GROUP_ID=${actionGroup.id}")
            )

            ShortcutInfoCompat.Builder(context, actionGroup.id)
                .setShortLabel(actionGroup.label)
                .setLongLabel("Execute ${actionGroup.label}")
                .setCategories(setOf(CATEGORY_ACTION_GROUP))
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_background))
                .setIntent(groupIntent)
                .build()
        }
    }

    companion object {
        private const val CATEGORY_ACTION_GROUP = "Action Groups"
    }
}