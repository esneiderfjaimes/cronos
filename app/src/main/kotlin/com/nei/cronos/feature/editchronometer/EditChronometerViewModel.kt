package com.nei.cronos.feature.editchronometer

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nei.cronos.utils.launchIO
import cronos.core.data.repository.LocalRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel(assistedFactory = EditChronometerViewModel.Factory::class)
class EditChronometerViewModel @AssistedInject constructor(
    private val localRepository: LocalRepository,
    @Assisted private val chronometerId: Long
) : ViewModel() {

    private var job: Job? = null
    private val _eventChannel = Channel<EditChronometerEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        Log.i(TAG, "init: chronometerId = $chronometerId")
    }

    fun update(label: String) {
        if (job != null) {
            Log.i(TAG, "update: already updating")
            return
        }
        job = launchIO {
            localRepository.updateChronometerLabel(chronometerId, label.trim())
        }.also {
            it.invokeOnCompletion {
                job = null
                launchIO {
                    _eventChannel.send(EditChronometerEvent.FinishUpdate)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(chronometerId: Long): EditChronometerViewModel
    }

    companion object {
        private const val TAG = "EditChronometerViewModel"
    }
}

sealed interface EditChronometerEvent {
    data object FinishUpdate : EditChronometerEvent
}
