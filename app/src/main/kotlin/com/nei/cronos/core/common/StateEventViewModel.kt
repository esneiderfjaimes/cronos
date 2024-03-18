package com.nei.cronos.core.common

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class StateEventViewModel<S, E>(firstState: S) : StateViewModel<S>(firstState) {
    protected val _eventChannel = Channel<E>()
    val events = _eventChannel.receiveAsFlow()
}