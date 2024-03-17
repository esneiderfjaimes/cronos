package com.nei.cronos.core.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class StateViewModel<T>(firstState: T) : ViewModel() {
    protected val _state: MutableStateFlow<T> = MutableStateFlow(firstState)
    val state: StateFlow<T> = _state

    protected inline fun updateState(function: T.() -> T) {
        _state.update(function)
    }
}