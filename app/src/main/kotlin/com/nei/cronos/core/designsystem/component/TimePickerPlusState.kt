@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.cronos.core.designsystem.component

import android.text.format.DateFormat
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
@ExperimentalMaterial3Api
fun rememberTimePickerPlusState(
    initialHour: Int? = null,
    initialMinute: Int? = null,
    initialShowDialog: Boolean = false,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    is24Hour: Boolean = is24HourFormat,
): TimePickerPlusState = rememberSaveable(
    saver = TimePickerPlusState.Saver()
) {
    val time = if (initialHour != null && initialMinute != null) {
        Time(
            initialHour,
            initialMinute,
            is24Hour
        )
    } else null
    val timePickerState = if (initialHour != null && initialMinute != null) {
        TimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = is24Hour
        )
    } else {
        val now = LocalTime.now()
        TimePickerState(
            initialHour = now.hour,
            initialMinute = now.minute,
            is24Hour = is24Hour
        )
    }
    TimePickerPlusState(
        timePickerState = timePickerState,
        initialShowDialog = initialShowDialog,
        initialDisplayMode = initialDisplayMode,
        initialTime = time
    )
}

data class Time(
    val hour: Int,
    val minute: Int,
    val is24hour: Boolean
) {
    fun format(): String {
        val localTime = LocalTime.of(hour, minute)
        val pattern = if (is24hour) "HH:mm" else "hh:mm a"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return localTime.format(formatter)
    }
}

internal val is24HourFormat: Boolean
    @Composable
    @ReadOnlyComposable get() = DateFormat.is24HourFormat(LocalContext.current)

@Immutable
@JvmInline
@ExperimentalMaterial3Api
value class DisplayMode internal constructor(internal val value: Int) {

    companion object {
        /** Date picker mode */
        val Picker = DisplayMode(0)

        /** Date text input mode */
        val Input = DisplayMode(1)
    }

    override fun toString() = when (this) {
        Picker -> "Picker"
        Input -> "Input"
        else -> "Unknown"
    }
}

@Stable
class TimePickerPlusState(
    val timePickerState: TimePickerState,
    initialShowDialog: Boolean = false,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    initialTime: Time? = null
) {
    var showingDialog by mutableStateOf(initialShowDialog)
    var displayMode by mutableStateOf(initialDisplayMode)
    var time by mutableStateOf(initialTime)

    fun toggleDisplayMode() {
        displayMode = if (displayMode == DisplayMode.Picker) {
            DisplayMode.Input
        } else {
            DisplayMode.Picker
        }
    }

    fun hideDialog() {
        showingDialog = false
    }

    companion object {
        /**
         * The default [Saver] implementation for [TimePickerPlusState].
         */
        fun Saver(): Saver<TimePickerPlusState, *> = Saver(
            save = {
                Log.i("TimePicker", "Saver: save: $it")
                listOf(
                    it.timePickerState.hour,
                    it.timePickerState.minute,
                    it.timePickerState.is24hour,
                    it.showingDialog,
                    it.displayMode.value,
                    it.time?.hour,
                    it.time?.minute,
                    it.time?.is24hour
                )
            },
            restore = { value ->
                Log.i("TimePicker", "Saver: restore: $value")
                TimePickerPlusState(
                    TimePickerState(
                        initialHour = value[0] as Int,
                        initialMinute = value[1] as Int,
                        is24Hour = value[2] as Boolean
                    ),
                    value[3] as Boolean,
                    DisplayMode(value[4] as Int),
                    run {
                        val hour = value[5] as? Int?
                        val minute = value[6] as? Int?
                        val is24hour = value[7] as? Boolean?
                        if (hour != null && minute != null && is24hour != null) {
                            Time(hour, minute, is24hour)
                        } else {
                            null
                        }
                    }
                )
            }
        )
    }
}