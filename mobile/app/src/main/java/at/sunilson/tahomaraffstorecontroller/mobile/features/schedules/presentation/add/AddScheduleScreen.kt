package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.components.ConfirmTextDialog
import at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.components.TimepickerDialog
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AddScheduleScreen(
    allActionGroups: ImmutableList<ExecutionActionGroup> = emptyList<ExecutionActionGroup>().toImmutableList(),
    selectedActionGroups: ImmutableSet<String> = emptySet<String>().toImmutableSet(),
    selectedDaysOfWeek: ImmutableSet<DayOfWeek> = emptySet<DayOfWeek>().toImmutableSet(),
    onlyOnSunshine: Boolean = false,
    selectedTime: LocalTime = LocalTime.now(),
    showSaveDialog: Boolean = false,
    saveDialogInputValue: String = "",
    onSaveDialogInputChanged: (String) -> Unit = {},
    onSaveDialogSubmitted: () -> Unit = {},
    onSaveDialogCancelled: () -> Unit = {},
    onActionGroupClicked: (ExecutionActionGroup) -> Unit = {},
    onDayOfWeekClicked: (DayOfWeek) -> Unit = {},
    onSaveButtonClicked: () -> Unit = {},
    onOnlyOnSunshineClicked: (Boolean) -> Unit = {},
    onTimeSelected: (LocalTime) -> Unit = {}
) {

    var showTimepicker by remember { mutableStateOf(false) }

    if (showTimepicker) {
        TimepickerDialog(
            onDismiss = { showTimepicker = false },
            onConfirm = {
                onTimeSelected(it)
                showTimepicker = false
            }
        )
    }

    if (showSaveDialog) {
        ConfirmTextDialog(
            title = "Choose schedule name",
            hint = "Enter name",
            text = saveDialogInputValue,
            onTextChanged = onSaveDialogInputChanged,
            onConfirm = onSaveDialogSubmitted,
            onDismiss = onSaveDialogCancelled
        )
    }

    Scaffold(floatingActionButton = {
        if(selectedActionGroups.isNotEmpty()) {
            FloatingActionButton(onClick = onSaveButtonClicked) {
                Icon(imageVector = Icons.Filled.Save, contentDescription = "Save schedule")
            }
        }
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item(key = "weekDaySelection") {
                Column {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = "Weekdays",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    DaySelectionRow(
                        selectedDays = selectedDaysOfWeek.toImmutableSet(),
                        onDayOfWeekClicked = onDayOfWeekClicked
                    )
                }
            }
            item(key = "timeSelection") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Time of day", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        modifier = Modifier.clickable { showTimepicker = true },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            item(key = "onlySunshineCheckbox") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Only when sunshine",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Checkbox(checked = onlyOnSunshine, onCheckedChange = onOnlyOnSunshineClicked)
                }
            }
            itemsIndexed(
                items = allActionGroups,
                key = { _, actionGroup -> actionGroup.id }
            ) { _, actionGroup ->
                SelectableActionGroupItem(
                    selected = selectedActionGroups.contains(actionGroup.id),
                    actionGroup = actionGroup,
                    onClick = onActionGroupClicked
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AddScheduleScreenPreview() {
    AddScheduleScreen()
}