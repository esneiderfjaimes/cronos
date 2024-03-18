package com.nei.cronos.ui.pages.addchronometer

import androidx.lifecycle.ViewModel
import com.nei.cronos.core.designsystem.component.Time
import com.nei.cronos.utils.launchIO
import cronos.core.data.repository.LocalRepository
import cronos.core.database.models.ChronometerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class AddChronometerViewModel @Inject constructor(
    private val localRepository: LocalRepository,
) : ViewModel() {

    fun insertChronometer(
        title: String,
        selectedTime: Time?,
        selectedDateMillis: Long?,
    ) {
        launchIO {
            val localDate = selectedDateMillis?.let {
                Instant.ofEpochMilli(selectedDateMillis).atZone(ZoneId.of("UTC")).toLocalDate()
            } ?: LocalDate.now()
            val localTime: LocalTime = if (selectedTime != null) {
                val (hour, minutes, _) = selectedTime
                LocalTime.of(hour, minutes)
            } else {
                LocalTime.now()
            }

            val localDateTime = LocalDateTime.of(localDate, localTime)
            val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
            localRepository.insertChronometer(
                ChronometerEntity(
                    title = title.trim(), allDateTime = zonedDateTime
                )
            )
        }
    }
}