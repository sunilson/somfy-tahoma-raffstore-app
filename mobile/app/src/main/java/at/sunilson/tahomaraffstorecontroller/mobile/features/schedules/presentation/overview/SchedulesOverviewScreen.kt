package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SchedulesOverviewScreen(
    schedules: ImmutableList<Schedule> = emptyList<Schedule>().toImmutableList(),
    onScheduleClicked: (String) -> Unit = {},
    onAddButtonClicked: () -> Unit = {},
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = onAddButtonClicked) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add schedule")
        }
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            itemsIndexed(
                items = schedules,
                key = { _, schedule -> schedule.id }
            ) { index, schedule ->
                ScheduleListItem(schedule = schedule, onScheduleClicked = onScheduleClicked, index != schedules.lastIndex)
            }
        }
    }
}