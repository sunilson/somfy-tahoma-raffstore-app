package at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle

interface CanUseSavedStateHandle<State> {
    val savedStateHandle: SavedStateHandle
    fun SavedStateHandle.restoreStateFromSavedStateHandle(): State
    fun SavedStateHandle.persistStateToSavedStateHandle(state: State)
}
