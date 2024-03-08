package com.nei.cronos.ui.pages.home

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.database.mappers.toUi
import com.nei.cronos.domain.models.SectionUi
import com.nei.cronos.utils.launchIO
import cronos.core.database.dao.SectionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    sectionDao: SectionDao,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        launchIO {
            sectionDao.sectEasionsVie2w()
                .map { sections ->
                    Log.d(TAG, "map(): $sections")
                    sections.toUi()
                }
                .onEach {
                    Log.d(TAG, "onEach(): $it")
                    updateState { copy(isLoading = false, sections = it) }
                }
                .catch { Log.e(TAG, "flowAllSections(): catch:", it) }
                .launchIn(viewModelScope)
        }
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