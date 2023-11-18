package com.nei.cronos.ui.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.database.ChronometerDao
import com.nei.cronos.core.database.models.ChronometerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddChronometerViewModel @Inject constructor(private val chronometerDao: ChronometerDao) :
    ViewModel() {
    fun insertChronometer(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val now = LocalDateTime.now()
            chronometerDao.insertAll(
                ChronometerEntity(title = title, createdAt = now, fromDate = now)
            )
        }
    }
}