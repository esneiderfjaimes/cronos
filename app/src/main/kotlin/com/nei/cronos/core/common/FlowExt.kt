package com.nei.cronos.core.common

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            Log.d("FlowExt", "update: $prevValue compared to $nextValue")
            return
        }
    }
}