package com.nei.cronos.core.database.models

import androidx.compose.runtime.Stable

@Stable
data class ChronometerFormat(
    val showSecond: Boolean = false,
    val showMinute: Boolean = false,
    val showHour: Boolean = false,
    val showDay: Boolean = false,
    val showWeek: Boolean = false,
    val showMonth: Boolean = false,
    val showYear: Boolean = false,
    val hideZeros: Boolean = false,
    val compactTimeEnabled: Boolean = false
) {
    fun toFlags(): Int {
        var flags = 0

        if (showSecond) flags = flags or (1 shl 0)
        if (showMinute) flags = flags or (1 shl 1)
        if (showHour) flags = flags or (1 shl 2)
        if (showDay) flags = flags or (1 shl 3)
        if (showWeek) flags = flags or (1 shl 4)
        if (showMonth) flags = flags or (1 shl 5)
        if (showYear) flags = flags or (1 shl 6)
        if (hideZeros) flags = flags or (1 shl 7)
        if (compactTimeEnabled) flags = flags or (1 shl 8)

        return flags
    }

    val timeFlagsEnabled: Boolean
        get() = showMinute && showHour

    val isAllFlagsDisabled: Boolean = !showSecond && !showMinute && !showHour
            && !showDay && !showWeek && !showMonth && !showYear

    companion object {

        fun fromFlags(flags: Int): ChronometerFormat {
            return ChronometerFormat(
                showSecond = (flags and (1 shl 0)) != 0,
                showMinute = (flags and (1 shl 1)) != 0,
                showHour = (flags and (1 shl 2)) != 0,
                showDay = (flags and (1 shl 3)) != 0,
                showWeek = (flags and (1 shl 4)) != 0,
                showMonth = (flags and (1 shl 5)) != 0,
                showYear = (flags and (1 shl 6)) != 0,
                hideZeros = (flags and (1 shl 7)) != 0,
                compactTimeEnabled = (flags and (1 shl 8)) != 0,
            )
        }

        val DefaultFormat = ChronometerFormat(
            showDay = true,
            showHour = true,
            showMinute = true,
            showSecond = true,
            hideZeros = true
        )
    }
}
