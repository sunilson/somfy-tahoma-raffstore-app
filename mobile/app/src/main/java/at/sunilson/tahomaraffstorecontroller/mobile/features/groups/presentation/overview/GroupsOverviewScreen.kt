package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.presentation.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import kotlinx.collections.immutable.ImmutableList

@Composable
fun GroupsOverviewScreen(
    actionGroups: ImmutableList<ExecutionActionGroup>,
    executions: ImmutableList<Execution>,
    favouriteGroups: ImmutableList<String>,
    onItemClicked: (ExecutionActionGroup) -> Unit,
    onDeleteActionGroupClicked: (ExecutionActionGroup) -> Unit,
    onStopExecutionClicked: (ExecutionActionGroup) -> Unit,
    onExecuteActionGroupClicked: (ExecutionActionGroup) -> Unit,
    onAddButtonClick: () -> Unit,
    onFavouriteClicked: (ExecutionActionGroup) -> Unit
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = onAddButtonClick) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add action group")
        }
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            itemsIndexed(items = actionGroups, key = { _, group -> group.id }) { index, actionGroup ->
                val executing = executions.any { execution -> execution.actionGroupLabel == actionGroup.label }
                GroupListItem(
                    actionGroup = actionGroup,
                    favouriteGroups = favouriteGroups,
                    executing = executing,
                    showDivider = index != actionGroups.lastIndex,
                    onExecuteActionGroupClicked = onExecuteActionGroupClicked,
                    onStopExecutionClicked = onStopExecutionClicked,
                    onDeleteActionGroupClicked = onDeleteActionGroupClicked,
                    onFavouriteClicked = onFavouriteClicked,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}