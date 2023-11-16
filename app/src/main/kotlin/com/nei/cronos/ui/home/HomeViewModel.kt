package com.nei.cronos.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.database.ChronometerDao
import com.nei.cronos.core.database.ChronometerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(chronometerDao: ChronometerDao) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)
            chronometerDao.flowAll()
                .catch { Log.e(TAG, "chronometerDao.flowAll(): catch:", it) }
                .collect { chronometers ->
                    _state.value = _state.value.copy(isLoading = false, chronometers = chronometers)
                }
        }
    }

    data class HomeState(
        val isLoading: Boolean = false,
        val chronometers: List<ChronometerEntity> = emptyList()
    )

    companion object {
        private const val TAG = "HomeViewModel"
    }
}