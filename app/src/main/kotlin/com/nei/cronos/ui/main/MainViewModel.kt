package com.nei.cronos.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.datastore.domain.SettingsRepository
import com.nei.cronos.core.datastore.domain.model.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = settingsRepository.getSettingsFlow().map {
        MainUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val settingsState: SettingsState) : MainUiState
}
