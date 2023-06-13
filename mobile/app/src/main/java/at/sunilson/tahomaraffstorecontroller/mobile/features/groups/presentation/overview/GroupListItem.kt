package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun GroupListItem(
    actionGroup: ExecutionActionGroup,
    favouriteGroups: ImmutableList<String> = emptyList<String>().toImmutableList(),
    executing: Boolean = false,
    showDivider: Boolean = false,
    onExecuteActionGroupClicked: (ExecutionActionGroup) -> Unit = {},
    onStopExecutionClicked: (ExecutionActionGroup) -> Unit = {},
    onDeleteActionGroupClicked: (ExecutionActionGroup) -> Unit = {},
    onFavouriteClicked: (ExecutionActionGroup) -> Unit = {},
    onItemClicked: (ExecutionActionGroup) -> Unit = {}
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemClicked(actionGroup) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(actionGroup.label, modifier = Modifier.weight(1f))
            IconButton(onClick = { onFavouriteClicked(actionGroup) }) {
                if (favouriteGroups.contains(actionGroup.id)) {
                    Icon(Icons.Default.Favorite, contentDescription = "Favorite")
                } else {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(
                onClick = { onDeleteActionGroupClicked(actionGroup) }
            ) { Icon(Icons.Default.Delete, contentDescription = "Stop execution") }
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(
                onClick = {
                    if (executing) {
                        onStopExecutionClicked(actionGroup)
                    } else {
                        onExecuteActionGroupClicked(actionGroup)
                    }
                }
            ) {
                if (executing) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop execution")
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Start execution")
                }
            }
        }
        if (showDivider) {
            Divider()
        }
    }
}