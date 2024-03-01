package com.nei.cronos.utils

import com.nei.cronos.core.database.models.ChronometerFormat
import java.time.Duration
import java.time.ZonedDateTime
import java.util.Locale

fun ZonedDateTime.differenceParse(
    format: ChronometerFormat,
    locale: Locale,
) = buildString {
    val now = ZonedDateTime.now(this@differenceParse.zone)
    var duration = Duration.between(this@differenceParse, now)

    val years = duration.toYears()
    if (format.showYear && (!format.hideZeros || (years != 0L))) {
        append("${years.format(locale)}y ")
        duration = duration.minusYears(years)
    }

    val months = duration.toMonths()
    if (format.showMonth && (!format.hideZeros || (months != 0L))) {
        append("${months.format(locale)}m ")
        duration = duration.minusMonths(months)
    }

    val weeks = duration.toWeeks()
    if (format.showWeek && (!format.hideZeros || (weeks != 0L))) {
        append("${weeks.format(locale)}w ")
        duration = duration.minusWeeks(weeks)
    }

    val days = duration.toDays()
    if (format.showDay && (!format.hideZeros || (days != 0L))) {
        append("${days.format(locale)}d ")
        duration = duration.minusDays(days)
    }

    if (format.showHour) {
        val hours = duration.toHours()
        if ((hours < 24) && format.showMinute && format.compactTimeEnabled) {
            val hoursPart = duration.toHoursPart().toString().padStart(2, '0')
            val minutesPart = duration.toMinutesPart().toString().padStart(2, '0')
            if (format.showSecond) {
                val secondsPart = duration.toSecondsPart().toString().padStart(2, '0')
                append("$hoursPart:$minutesPart:$secondsPart ") // HH:MM:SS
            } else {
                append("$hoursPart:$minutesPart ") // HH:MM
            }
            return@buildString
        } else if (!format.hideZeros || (hours != 0L)) {
            append("${hours.format(locale)}h ")
            duration = duration.minusHours(hours)
        }
    }

    val minutes = duration.toMinutes()
    if (format.showMinute && (!format.hideZeros || (minutes != 0L))) {
        append("${minutes.format(locale)}m ")
        duration = duration.minusMinutes(minutes)
    }

    if (format.showSecond) {
        val seconds = duration.toSeconds()
        append("${seconds.format(locale)}s ")
    }
}