package com.nei.cronos.ui.pages.chronometer

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nei.cronos.core.data.LocalRepository
import com.nei.cronos.core.database.mappers.toDomain
import com.nei.cronos.core.database.mappers.toUi
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.ui.pages.chronometer.navigation.ChronometerArgs
import com.nei.cronos.utils.differenceParse
import com.nei.cronos.utils.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import java.time.Instant
import java.util.Timer
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.concurrent.timer

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
            localRepository.chronometerWithLapsById(args.chronometerId).catch {
                Log.e(TAG, "flowChronometerWithLapsById(${args.chronometerId}): catch:", it)
            }.collect { chronometerWithLaps ->
                val uiState = _state.value
                _state.value = chronometerWithLaps?.let {
                    _currentTime.value = it.chronometer.fromDate.differenceParse(
                        it.chronometer.format,
                        currentLocale
                    )
                    if (uiState is ChronometerUiState.Success) {
                        uiState.copy(chronometer = it.chronometer.toUi())
                    } else {
                        ChronometerUiState.Success(chronometer = it.chronometer.toUi())
                    }
                } ?: ChronometerUiState.Error
            }
        }
        /*        val timer = Timer()
                timer.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        val uiState = _state.value
                        if (uiState is ChronometerUiState.Success) {
                            _currentTime.value = uiState.chronometer.fromDate.differenceParse(
                                uiState.chronometer.format,
                                currentLocale
                            )
                        }
                    }
                }, 0, 1000)*/

        startTimer()
    }


    private fun startTimer() {
        val atomicBoolean = AtomicBoolean(false)
        timer = timer(period = 1000) {
            if (atomicBoolean.compareAndSet(false, true)) {
                Log.d(TAG, "Tarea iniciada en: ${Instant.now()}")
                val uiState = _state.value
                if (uiState is ChronometerUiState.Success) {
                    _currentTime.value = uiState.chronometer.fromDate.differenceParse(
                        uiState.chronometer.format,
                        currentLocale
                    )
                }

                atomicBoolean.set(false) // Set completed task
                Log.d(TAG, "Tiempo transcurrido: ${Instant.now()} ms")
            } else {
                Log.e(TAG, "Tarea previa aún en progreso, cancelando esta instancia.")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    fun onUpdateChronometer(chronometer: ChronometerUi) {
        launchIO {
            val uiState = _state.value
            if (uiState is ChronometerUiState.Success) {
                if (chronometer.format.isAllFlagsDisabled) {
                    _state.value = uiState.copy(
                        chronometer = chronometer.copy(
                            format = chronometer.format.copy(
                                showSecond = true
                            )
                        )
                    )
                } else {
                    _state.value = uiState.copy(chronometer = chronometer)
                }
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

    fun onNewLapClick(chronometer: ChronometerUi) {
        launchIO {
            localRepository.registerLapIn(chronometer.toDomain())
        }
    }

    companion object {
        const val TAG = "ChronometerViewModel"
    }
}

sealed interface ChronometerUiState {

    data class Success(
        val chronometer: ChronometerUi,
        private val chronometerPreviews: ChronometerUi = chronometer,
    ) : ChronometerUiState {
        val enabledSaveButton: Boolean =
            (chronometerPreviews.format != chronometer.format)
                    && !chronometer.format.isAllFlagsDisabled
    }

    object Error : ChronometerUiState
    object Loading : ChronometerUiState
}

