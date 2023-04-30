package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsOverviewScreen(
    actionGroups: ImmutableList<LocalExecutionActionGroup>,
    executions: ImmutableList<Execution>,
    favouriteGroups: ImmutableList<String>,
    onItemClicked: (LocalExecutionActionGroup) -> Unit,
    onDeleteActionGroupClicked: (LocalExecutionActionGroup) -> Unit,
    onStopExecutionClicked: (LocalExecutionActionGroup) -> Unit,
    onExecuteActionGroupClicked: (LocalExecutionActionGroup) -> Unit,
    onAddButtonClick: () -> Unit,
    onFavouriteClicked: (LocalExecutionActionGroup) -> Unit
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = onAddButtonClick) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add action group")
        }
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            actionGroups.forEach { actionGroup ->
                item(key = actionGroup.id) {
                    val executing = executions.any { execution -> execution.actionGroup.label == actionGroup.label }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClicked(actionGroup) }
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
                }
            }
        }
    }
}