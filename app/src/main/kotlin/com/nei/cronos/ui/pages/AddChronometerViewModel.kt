package com.nei.cronos.ui.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.models.ChronometerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

typealias Time = Triple<Int, Int, Boolean>

@HiltViewModel
class AddChronometerViewModel @Inject constructor(
    private val chronometerDao: ChronometerDao,
) : ViewModel() {

    fun insertChronometer(
        title: String,
        selectedTime: Time?,
        selectedDateMillis: Long?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val localDate = selectedDateMillis?.let {
                Instant.ofEpochMilli(selectedDateMillis).atZone(ZoneId.of("UTC")).toLocalDate()
            } ?: LocalDate.now()
            val localTime: LocalTime = if (selectedTime != null) {
                val (hour, minutes, is24hour) = selectedTime
                LocalTime.of(hour, minutes)
            } else {
                LocalTime.now()
            }

            val localDateTime = LocalDateTime.of(localDate, localTime)

            val createdAt = ZonedDateTime.now()
            val fromDate = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
            chronometerDao.insertAll(
                ChronometerEntity(
                    title = title, createdAt = createdAt, fromDate = fromDate
                )
            )
        }
    }
}