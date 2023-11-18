package com.nei.cronos.utils

import com.nei.cronos.core.database.models.ChronometerFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.Locale

fun LocalDateTime.differenceParse(
    format: ChronometerFormat,
    locale: Locale,
) = buildString {
    val now = LocalDateTime.now()
    var diff = Duration.between(this@differenceParse, now)
    val years = diff.toYears()
    if (format.showYear && (!format.hideZeros || years != 0L)) {
        append("${years.format(locale)}y ")
        diff = diff.minusYears(years)
    }

    val months = diff.toMonths()
    if (format.showMonth && (!format.hideZeros || months != 0L)) {
        append("${months.format(locale)}m ")
        diff = diff.minusMonths(months)
    }

    val weeks = diff.toWeeks()
    if (format.showWeek && (!format.hideZeros || weeks != 0L)) {
        append("${weeks.format(locale)}w ")
        diff = diff.minusWeeks(weeks)
    }

    val days = diff.toDays()
    if (format.showDay && (!format.hideZeros || days != 0L)) {
        append("${days.format(locale)}d ")
        diff = diff.minusDays(days)
    }

    if (format.showHour) {
        val hours = diff.toHours()
        if (hours < 24 && format.showMinute) {
            val hoursPart = diff.toHoursPart().toString().padStart(2, '0')
            val minutesPart = diff.toMinutesPart().toString().padStart(2, '0')
            if (format.showSecond) {
                val secondsPart = diff.toSecondsPart().toString().padStart(2, '0')
                append("$hoursPart:$minutesPart:$secondsPart ") // HH:MM:SS
            } else {
                append("$hoursPart:$minutesPart ") // HH:MM
            }
            return@buildString
        } else {
            append("${hours.format(locale)}h ")
            diff = diff.minusHours(hours)
        }
    }

    if (format.showMinute) {
        val minutes = diff.toMinutes()
        append("${minutes.format(locale)}m ")
        diff = diff.minusMinutes(minutes)
    }

    if (format.showSecond) {
        val seconds = diff.toSeconds()
        append("${seconds.format(locale)}s ")
    }
}