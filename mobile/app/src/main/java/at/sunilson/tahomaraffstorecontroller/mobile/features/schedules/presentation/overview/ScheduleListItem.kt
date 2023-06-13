package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleListItem(schedule: Schedule, onScheduleClicked: (String) -> Unit, showBottomDivider: Boolean = false) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onScheduleClicked(schedule.id) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(schedule.label, modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(text = schedule.weekdays.joinToString { it.name.take(2) })
                Text(text = schedule.time.format(DateTimeFormatter.ofPattern("HH:mm")))
            }
        }
        if (showBottomDivider) {
            Divider()
        }
    }
}