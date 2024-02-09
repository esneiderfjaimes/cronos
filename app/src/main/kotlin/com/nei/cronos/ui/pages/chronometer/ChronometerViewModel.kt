package com.nei.cronos.ui.pages.chronometer

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.ChronometerWithLapsDao
import com.nei.cronos.core.database.daos.LapDao
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.LapEntity
import com.nei.cronos.ui.home.HomeViewModel
import com.nei.cronos.ui.pages.chronometer.navigation.ChronometerArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ChronometerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chronometerDao: ChronometerDao,
    private val lapsDao: LapDao,
    private val chronometerWithLapsDao: ChronometerWithLapsDao
) : ViewModel() {
    private val args: ChronometerArgs = ChronometerArgs(savedStateHandle)

    private val _state = MutableStateFlow<ChronometerUiState>(ChronometerUiState.Loading)
    val state: StateFlow<ChronometerUiState> = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            chronometerWithLapsDao.byId(args.chronometerId).catch {
                Log.e(TAG, "chronometerWithLapsDao.byId(${args.chronometerId}): catch:", it)
            }.collect { chronometerWithLaps ->
                _state.value = chronometerWithLaps?.let { ChronometerUiState.Success(it.chronometer) }
                    ?: ChronometerUiState.Error
            }
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

    fun onNewLapClick(chronometer: ChronometerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            // create new lap
            val newLap = LapEntity(chronometerId = chronometer.id)
            lapsDao.insert(newLap)
            // reset fromDate to now
            chronometerDao.update(chronometer.copy(fromDate = ZonedDateTime.now()))
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

