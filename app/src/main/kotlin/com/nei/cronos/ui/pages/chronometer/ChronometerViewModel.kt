package com.nei.cronos.ui.pages.chronometer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.database.ChronometerDao
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.ui.pages.chronometer.navigation.ChronometerArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChronometerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chronometerDao: ChronometerDao,
) : ViewModel() {
    private val args: ChronometerArgs = ChronometerArgs(savedStateHandle)

    private val _state = MutableStateFlow<ChronometerUiState>(ChronometerUiState.Loading)
    val state: StateFlow<ChronometerUiState> = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val chronometer = chronometerDao.byId(args.chronometerId)
            _state.value = ChronometerUiState.Success(chronometer)
        }
    }

    fun onUpdateChronometer(chronometer: ChronometerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_state.value is ChronometerUiState.Success) {
                _state.value = (_state.value as ChronometerUiState.Success)
                    .copy(chronometer = chronometer)
            }
        }
    }

    fun onSaveClick() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_state.value is ChronometerUiState.Success) {
                val success = _state.value as ChronometerUiState.Success
                if (success.enabledSaveButton) {
                    val chronometer = success.chronometer
                    chronometerDao.update(chronometer)
                }
            }
        }
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

