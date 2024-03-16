package com.nei.cronos.feature.editchronometerformat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.utils.launchIO
import cronos.core.data.repository.LocalRepository
import cronos.core.database.models.EventEntity
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZonedDateTime
import java.util.Timer
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.fixedRateTimer

@HiltViewModel(assistedFactory = EditChronometerFormatViewModel.Factory::class)
class EditChronometerFormatViewModel @AssistedInject constructor(
    private val localRepository: LocalRepository,
    @Assisted private val chronometer: ChronometerUi,
    @Assisted private val lastEvent: EventEntity? = null,
) : ViewModel() {

    private val _state = MutableStateFlow(State(chronometer, lastEvent))
    val state get() = _state.asStateFlow()

    private val _eventChannel = Channel<Event>()
    val events = _eventChannel.receiveAsFlow()

    private var job: Job? = null
    private var timer: Timer? = null

    init {
        Log.i(TAG, "init: chronometer:$chronometer lastEvent:$lastEvent")
        if (!chronometer.isActive && lastEvent != null && lastEvent.type == EventType.STOP) {
            timer?.cancel()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        Log.i(TAG, "startTimer")
        val atomicBoolean = AtomicBoolean(false)
        timer?.cancel()
        timer = fixedRateTimer(period = 1000) {
            launchIO {
                if (atomicBoolean.compareAndSet(false, true)) {
                    Log.d(TAG, "Task started: ${Instant.now()}")
                    forceUpdateLabelTimer()
                    atomicBoolean.set(false) // Set completed task
                } else {
                    Log.e(TAG, "Previews task already running, ignoring.")
                }
            }
        }
    }

    private suspend fun forceUpdateLabelTimer() {
        withContext(Dispatchers.IO) {
            _state.value = _state.value.copy(
                timeRanges = getRanges(chronometer, lastEvent)
            )
        }
    }

    fun onFormatChange(chronometerFormat: ChronometerFormat) {
        _state.value = _state.value.copy(format = chronometerFormat)
    }

    fun updateFormat() {
        if (job != null) {
            Log.i(TAG, "update: already updating")
            return
        }
        job = launchIO {
            localRepository.updateChronometerFormat(chronometer.id, _state.value.format)
        }.also {
            it.invokeOnCompletion {
                job = null
                launchIO {
                    _eventChannel.send(Event.FinishUpdate)
                }
            }
        }
    }

    public override fun onCleared() {
        super.onCleared()
        _state.value = State(chronometer, lastEvent)
        Log.i(TAG, "onCleared: ")
    }

    @AssistedFactory
    interface Factory {
        fun create(
            chronometer: ChronometerUi,
            lastEvent: EventEntity? = null,
        ): EditChronometerFormatViewModel
    }

    sealed interface Event {
        data object FinishUpdate : Event
    }

    data class State(
        val format: ChronometerFormat,
        val timeRanges: Pair<ZonedDateTime, ZonedDateTime>
    ) {
        constructor(
            chronometer: ChronometerUi,
            lastEvent: EventEntity?,
        ) : this(chronometer.format, getRanges(chronometer, lastEvent))
    }

    companion object {
        private const val TAG = "EditChronometerViewModel"

        fun getRanges(
            chronometer: ChronometerUi,
            lastEvent: EventEntity? = null
        ): Pair<ZonedDateTime, ZonedDateTime> {
            return when {
                lastEvent == null -> chronometer.startDate to ZonedDateTime.now()
                !chronometer.isActive && lastEvent.type == EventType.STOP ->
                    chronometer.startDate to lastEvent.time

                else -> lastEvent.time to ZonedDateTime.now()
            }
        }
    }
}
