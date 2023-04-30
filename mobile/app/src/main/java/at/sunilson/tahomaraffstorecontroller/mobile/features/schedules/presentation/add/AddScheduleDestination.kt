package at.sunilson.tahomaraffstorecontroller.mobile.features.schedules.presentation.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddScheduleDestination() {
    val viewModel = koinViewModel<AddScheduleViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
}