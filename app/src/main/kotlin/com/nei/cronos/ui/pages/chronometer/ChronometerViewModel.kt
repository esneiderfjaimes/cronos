package com.nei.cronos.ui.pages.chronometer

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nei.cronos.core.data.LocalRepository
import com.nei.cronos.core.database.models.ChronometerEntity
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
            localRepository.flowChronometerWithLapsById(args.chronometerId).catch {
                Log.e(TAG, "flowChronometerWithLapsById(${args.chronometerId}): catch:", it)
            }.collect { chronometerWithLaps ->
                _state.value =
                    chronometerWithLaps?.let { ChronometerUiState.Success(it.chronometer) }
                        ?: ChronometerUiState.Error
            }
        }
    }

    fun onUpdateChronometer(chronometer: ChronometerEntity) {
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
                    localRepository.insertChronometer(uiState.chronometer)
                }
            }
        }
    }

    fun onNewLapClick(chronometer: ChronometerEntity) {
        launchIO {
            localRepository.registerLapIn(chronometer)
        }
    }

    companion object {
        const val TAG = "ChronometerViewModel"
    }
}

sealed interface ChronometerUiState {

    data class Success(
        val chronometer: ChronometerEntity,
        private val chronometerPreviews: ChronometerEntity = chronometer,
    ) : ChronometerUiState {
        val enabledSaveButton: Boolean = chronometerPreviews != chronometer
    }

    object Error : ChronometerUiState
    object Loading : ChronometerUiState
}

