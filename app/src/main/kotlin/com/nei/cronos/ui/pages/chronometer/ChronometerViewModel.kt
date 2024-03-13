package com.nei.cronos.ui.pages.chronometer

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cronos.core.data.repository.LocalRepository
import com.nei.cronos.core.database.mappers.toDomain
import com.nei.cronos.core.database.mappers.toUi
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.ui.pages.chronometer.navigation.ChronometerArgs
import com.nei.cronos.utils.differenceParse
import com.nei.cronos.utils.launchIO
import cronos.core.database.embeddeds.ChronometerWithLastEvent
import cronos.core.database.models.EventEntity
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.Timer
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ChronometerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val localRepository: LocalRepository
) : ViewModel() {
    private val currentLocale = context.resources.configuration.locales[0]

    private val args: ChronometerArgs = ChronometerArgs(savedStateHandle)

    private val _state = MutableStateFlow<ChronometerUiState>(ChronometerUiState.Loading)
    val state: StateFlow<ChronometerUiState> = _state

    private val _currentTime = MutableStateFlow("❤️‍🔥")
    val currentTime: StateFlow<String> = _currentTime

    private var timer: Timer? = null

    init {
        launchIO {
            localRepository.chronometerWithLastEventById(args.chronometerId).catch {
                Log.e(TAG, "collect(${args.chronometerId}): catch:", it)
            }.collect(this@ChronometerViewModel::collector)
        }
    }

    private fun collector(query: ChronometerWithLastEvent?) {
        Log.d(TAG, "collect: $query")
        if (query == null) {
            _state.value = ChronometerUiState.Error
            return
        }

        val newState = ChronometerUiState.Success(
            chronometer = query.chronometer.toUi(),
            lastEvent = query.lastEvent
        )

        updateLabel(newState)

        if (newState.isPaused) {
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

    private suspend fun forceUpdateLabelTimer() {
        withContext(Dispatchers.IO) {
            val uiState = _state.value
            if (uiState is ChronometerUiState.Success) {
                updateLabel(uiState)
            }
        }
    }

    private fun updateLabel(uiState: ChronometerUiState.Success) {
        val chronometer = uiState.chronometer
        val last = uiState.lastEvent
        _currentTime.value = if (last != null) {
            if (uiState.isPaused) {
                differenceParse(
                    chronometer.format,
                    currentLocale,
                    chronometer.startDate,
                    last.time
                )
            } else {
                last.time.differenceParse(
                    chronometer.format,
                    currentLocale
                )
            }
        } else {
            chronometer.startDate.differenceParse(
                chronometer.format,
                currentLocale
            )
        }
    }

    fun updateFormat(format: ChronometerFormat) {
        launchIO {
            val uiState = _state.value
            if (uiState is ChronometerUiState.Success) {
                if (format.isAllFlagsDisabled) {
                    _state.value = uiState.copy(
                        chronometer = uiState.chronometer.copy(
                            format = format.copy(showSecond = true)
                        )
                    )
                } else {
                    _state.value = uiState.copy(
                        chronometer = uiState.chronometer.copy(format = format)
                    )
                }
                forceUpdateLabelTimer()
            }
        }
    }

    fun onSaveClick() {
        launchIO {
            val uiState = _state.value
            if (uiState is ChronometerUiState.Success) {
                if (uiState.enabledSaveButton) {
                    localRepository.updateChronometer(uiState.chronometer.toDomain())
                }
            }
        }
    }

    fun onNewLapClick(chronometer: ChronometerUi, eventType: EventType) {
        launchIO {
            localRepository.registerEventIn(chronometer.toDomain(), eventType = eventType)
        }
    }

    fun deleteChronometer() {
        launchIO {
            localRepository.updateChronometerIsActive(
                id = args.chronometerId,
                isArchived = true
            ).also {
                Log.i(TAG, "deleteChronometer: $it")
            }
        }
    }

    companion object {
        const val TAG = "ChronometerViewModel"
    }
}

sealed interface ChronometerUiState {

    data class Success(
        val chronometer: ChronometerUi,
        val lastEvent: EventEntity? = null,
        private val previousFormat: ChronometerFormat = chronometer.format,
    ) : ChronometerUiState {
        val enabledSaveButton: Boolean =
            (previousFormat != chronometer.format && !chronometer.format.isAllFlagsDisabled)

        val isPaused: Boolean =
            (!chronometer.isActive && lastEvent?.type == EventType.STOP)
    }

    data object Error : ChronometerUiState
    data object Loading : ChronometerUiState
}

