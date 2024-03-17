package com.nei.cronos.ui.pages.chronometer

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.nei.cronos.core.common.StateEventViewModel
import com.nei.cronos.core.common.genTimeRange
import com.nei.cronos.core.database.mappers.toDomain
import com.nei.cronos.core.database.mappers.toUi
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.ui.pages.chronometer.navigation.ChronometerArgs
import com.nei.cronos.utils.launchIO
import cronos.core.data.repository.LocalRepository
import cronos.core.database.embeddeds.ChronometerWithLastEvent
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import java.time.Instant
import java.time.ZonedDateTime
import java.util.Timer
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ChronometerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val localRepository: LocalRepository
) : StateEventViewModel<ChronometerViewModel.UiState, ChronometerViewModel.Event>(UiState.Loading) {
    private val args: ChronometerArgs = ChronometerArgs(savedStateHandle)

    private var job: Job? = null
    private var timer: Timer? = null

    init {
        launchIO {
            localRepository.chronometerWithLastEventById(args.chronometerId).catch {
                Log.e(TAG, "collect(${args.chronometerId}): catch:", it)
                _state.value = UiState.Error
            }.collect(this@ChronometerViewModel::collector)
        }
    }

    private fun collector(query: ChronometerWithLastEvent?) {
        Log.d(TAG, "collect: $query")
        if (query == null) {
            _state.value = UiState.Error
            return
        }

        val chronometerUi = query.toUi()
        val newState = UiState.Success(
            chronometer = chronometerUi, // update chronometer
            tempFormat = chronometerUi.format, // reset tempFormat when chronometer is changed
            timeRanges = genTimeRange(chronometerUi) // update timeRanges
        )

        updateLabel(newState)

        if (chronometerUi.isPaused) {
            timer?.cancel()
        } else {
            startTimer()
        }
        _state.value = newState
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

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun forceUpdateLabelTimer() {
        val uiState = _state.value
        if (uiState is UiState.Success) {
            updateLabel(uiState)
        }
    }

    private fun updateLabel(uiState: UiState.Success) {
        _state.value = uiState.copy(
            timeRanges = genTimeRange(chronometer = uiState.chronometer)
        )
    }

    fun onFormatChange(format: ChronometerFormat) {
        launchIO {
            updateIfSuccess {
                if (format.isAllFlagsDisabled) {
                    copy(tempFormat = format.copy(showSecond = true))
                } else {
                    copy(tempFormat = format)
                }.also { forceUpdateLabelTimer() }
            }
        }
    }

    fun addEvent(eventType: EventType) {
        launchIO {
            val uiState = _state.value
            if (uiState !is UiState.Success) return@launchIO
            localRepository.registerEventIn(uiState.chronometer.toDomain(), eventType = eventType)
        }
    }

    fun deleteChronometer() {
        launchIO {
            localRepository.updateChronometerIsActive(
                id = args.chronometerId,
                isArchived = true
            ).also { Log.i(TAG, "deleteChronometer: $it") }
        }
    }

    fun updateFormat() {
        if (job != null) {
            Log.i(TAG, "update: already updating")
            return
        }
        val uiState = _state.value
        if (uiState is UiState.Success) {
            job = launchIO {
                localRepository.updateChronometerFormat(uiState.chronometer.id, uiState.tempFormat)
            }.also {
                it.invokeOnCompletion {
                    job = null
                    launchIO {
                        _eventChannel.send(Event.FinishUpdate)
                    }
                }
            }
        }
    }

    fun onConfirmationDiscardChanges() {
        updateIfSuccess { copy(tempFormat = chronometer.format) }
    }

    private fun updateIfSuccess(function: UiState.Success.() -> UiState.Success) {
        val uiState = _state.value
        if (uiState is UiState.Success) {
            updateState { uiState.function() }
        }
    }

    companion object {
        const val TAG = "ChronometerViewModel"
    }

    sealed interface UiState {
        data class Success(
            val chronometer: ChronometerUi,
            val tempFormat: ChronometerFormat,
            val timeRanges: Pair<ZonedDateTime, ZonedDateTime>
        ) : UiState {
            constructor(
                chronometer: ChronometerUi,
            ) : this(
                chronometer = chronometer,
                tempFormat = chronometer.format,
                timeRanges = genTimeRange(chronometer)
            )
        }

        data object Error : UiState
        data object Loading : UiState
    }

    sealed interface Event {
        data object FinishUpdate : Event
    }
}
