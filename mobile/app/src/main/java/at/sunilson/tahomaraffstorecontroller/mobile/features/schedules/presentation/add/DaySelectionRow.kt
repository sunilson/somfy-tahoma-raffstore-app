package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import java.time.DayOfWeek

@Composable
fun DaySelectionRow(
    selectedDays: ImmutableSet<DayOfWeek> = emptySet<DayOfWeek>().toImmutableSet(),
    onDayOfWeekClicked: (DayOfWeek) -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        DayOfWeek.values().forEach {
            DayCheckbox(dayOfWeek = it, selectedDays = selectedDays, onDayOfWeekClicked = onDayOfWeekClicked)
        }
    }
}

@Composable
private fun RowScope.DayCheckbox(
    dayOfWeek: DayOfWeek,
    selectedDays: ImmutableSet<DayOfWeek>,
    onDayOfWeekClicked: (DayOfWeek) -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = selectedDays.contains(dayOfWeek),
            onCheckedChange = { onDayOfWeekClicked(dayOfWeek) },
        )
        Text(text = dayOfWeek.name.take(2))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DaySelectionRowPreview() {
    DaySelectionRow(
        selectedDays = setOf(DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY).toImmutableSet()
    )
}