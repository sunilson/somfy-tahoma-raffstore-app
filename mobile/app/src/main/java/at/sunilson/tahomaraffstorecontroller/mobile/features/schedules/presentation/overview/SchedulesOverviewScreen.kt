package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SchedulesOverviewScreen(
    onAddButtonClick: () -> Unit = {}
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = onAddButtonClick) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add schedule")
        }
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {

        }
    }
}