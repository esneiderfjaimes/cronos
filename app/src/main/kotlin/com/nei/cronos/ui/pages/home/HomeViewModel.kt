package com.nei.cronos.ui.pages.home

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.data.LocalRepository
import com.nei.cronos.core.database.mappers.toUi
import com.nei.cronos.domain.models.SectionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    localRepository: LocalRepository
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