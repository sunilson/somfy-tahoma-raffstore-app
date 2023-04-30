package at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, SideEffect>(initialState: State) : ViewModel() {

    private val _state = MutableStateFlow(initialState)

    /**
     * The state of this ViewModel. Will be replayed on subscription and is distinct
     */
    val state: StateFlow<State> = _state.asStateFlow()

    /**
     * [state] flow consumed as [LiveData]
     */
    val stateLiveData: LiveData<State>
        get() = state.asLiveData(viewModelScope.coroutineContext)

    private val _sideEffects = MutableSharedFlow<SideEffect>()

    /**
     * One time events that are broadcast to all subscribers without replay
     */
    val sideEffects: SharedFlow<SideEffect> = _sideEffects.asSharedFlow()

    init {
        tryUsingSavedStateHandle()
    }

    private fun tryUsingSavedStateHandle() {
        // Check if CanUseSavedStateHandle was implemented, if yes we can try to save/restore state
        this as? CanUseSavedStateHandle<State> ?: return

        // Continously collect state and persist to saved state handle
        viewModelScope.launch {
            state.collect { savedStateHandle.persistStateToSavedStateHandle(it) }
        }

        // Restore from saved state handle once on startup
        reduce { savedStateHandle.restoreStateFromSavedStateHandle() }
    }

    /**
     * Used to produce new state from current state. Is atomic, so the given reducer could be
     * invoked multiple times
     */
    protected fun reduce(reducer: (State) -> State) {
        _state.update(reducer)
    }

    /**
     * Used to broadcast one-off events to all subscribers like showing toasts or error dialogs
     */
    protected fun postSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            _sideEffects.emit(sideEffect)
        }
    }

    @Composable
    fun collectSideEffect(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        sideEffect: (suspend (sideEffect: SideEffect) -> Unit)
    ) {
        val sideEffectFlow = sideEffects
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(sideEffectFlow, lifecycleOwner) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
                sideEffectFlow.collect { sideEffect(it) }
            }
        }
    }
}
