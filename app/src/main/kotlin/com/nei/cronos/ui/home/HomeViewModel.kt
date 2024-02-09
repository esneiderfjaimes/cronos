package com.nei.cronos.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.ChronometerWithLapsDao
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.ChronometerFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chronometerDao: ChronometerDao,
    chronometerWithLapsDao: ChronometerWithLapsDao
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Load chronometerWithLaps
            chronometerWithLapsDao.flowAll()
                .catch { Log.e(TAG, "chronometerWithLapsDao.flowAll(): catch:", it) }
                .collect { chronometerWithLaps ->
                    Log.i(TAG, "chronometerWithLaps: $chronometerWithLaps")
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            // Load chronometers
            _state.value = _state.value.copy(isLoading = true)
            chronometerDao.flowAll()
                .catch { Log.e(TAG, "chronometerDao.flowAll(): catch:", it) }
                .collect { chronometers ->
                    if (chronometers.isEmpty()) {
                        mockIfEmpty()
                    }
                    _state.value = _state.value.copy(isLoading = false, chronometers = chronometers)
                }
        }
    }

    private fun mockIfEmpty() {
        chronometerDao.insertAll(
            ChronometerEntity(
                title = "since I've been using the app",
            ),
            ChronometerEntity(
                title = "avocado 🥑",
                fromDate = ZonedDateTime.of(
                    /* year = */ 2015,
                    /* month = */ 9,
                    /* dayOfMonth = */ 2,
                    /* hour = */ 0,
                    /* minute = */ 0,
                    /* second = */ 0,
                    /* nanoOfSecond = */ 0,
                    /* zone = */ ZoneId.systemDefault()
                ),
                format = ChronometerFormat.DefaultFormat.copy(
                    showYear = true,
                    showMonth = true,
                    showWeek = true
                )
            )
        )
    }

    data class HomeState(
        val isLoading: Boolean = false,
        val chronometers: List<ChronometerEntity> = emptyList(),
    )

    companion object {
        private const val TAG = "HomeViewModel"
    }
}