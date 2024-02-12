package com.nei.cronos.ui.home

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.data.LocalRepository
import com.nei.cronos.core.database.mappers.toUi
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.ChronometerFormat
import com.nei.cronos.core.database.models.SectionWithChronometers
import com.nei.cronos.domain.models.SectionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        localRepository.sectionsWithChronometers()
            .map { sections -> sections.map { it.toUi() } }
            .onEach { updateState { copy(isLoading = false, sections = it) } }
            .catch { Log.e(TAG, "flowAllSections(): catch:", it) }
            .launchIn(viewModelScope)
    }

    private suspend fun mockIfEmpty() {
        localRepository.insertChronometer(
            ChronometerEntity(
                title = "since I've been using the app",
            ),
            ChronometerEntity(
                title = "avocado 🥑",
                allDateTime = ZonedDateTime.of(
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

    private fun updateState(block: HomeState.() -> HomeState) {
        _state.value = block.invoke(_state.value)
    }

    @Stable
    data class HomeState(
        val isLoading: Boolean = false,
        val sections: List<SectionUi> = emptyList(),
    )

    companion object {
        private const val TAG = "HomeViewModel"
    }
}