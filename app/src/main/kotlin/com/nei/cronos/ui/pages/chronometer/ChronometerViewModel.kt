package com.nei.cronos.ui.pages.chronometer

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nei.cronos.core.data.LocalRepository
import com.nei.cronos.core.database.mappers.toDomain
import com.nei.cronos.core.database.mappers.toUi
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.ui.pages.chronometer.navigation.ChronometerArgs
import com.nei.cronos.utils.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class ChronometerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val localRepository: LocalRepository
) : ViewModel() {
    private val args: ChronometerArgs = ChronometerArgs(savedStateHandle)

    private val _state = MutableStateFlow<ChronometerUiState>(ChronometerUiState.Loading)
    val state: StateFlow<ChronometerUiState> = _state

    init {
        launchIO {
            localRepository.chronometerWithLapsById(args.chronometerId).catch {
                Log.e(TAG, "flowChronometerWithLapsById(${args.chronometerId}): catch:", it)
            }.collect { chronometerWithLaps ->
                val uiState = _state.value
                _state.value = chronometerWithLaps?.let {
                    if (uiState is ChronometerUiState.Success) {
                        uiState.copy(chronometer = it.chronometer.toUi())
                    } else {
                        ChronometerUiState.Success(chronometer = it.chronometer.toUi())
                    }
                } ?: ChronometerUiState.Error
            }
        }
    }

    fun onUpdateChronometer(chronometer: ChronometerUi) {
        launchIO {
            val uiState = _state.value
            if (uiState is ChronometerUiState.Success) {
                _state.value = uiState.copy(chronometer = chronometer)
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
        val enabledSaveButton: Boolean = chronometerPreviews.format != chronometer.format
    }

    object Error : ChronometerUiState
    object Loading : ChronometerUiState
}

