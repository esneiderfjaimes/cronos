package com.nei.cronos.utils

import android.os.Build
import cronos.core.model.ChronometerFormat
import java.time.Duration
import java.time.ZonedDateTime
import java.util.Locale

fun ZonedDateTime.differenceParse(
    format: ChronometerFormat,
    locale: Locale,
): String {
    val now = ZonedDateTime.now(this@differenceParse.zone)
    return differenceParse(format, locale, this, now)
}

fun differenceParse(
    format: ChronometerFormat,
    locale: Locale,
    startInclusive: ZonedDateTime,
    endExclusive: ZonedDateTime = ZonedDateTime.now(),
) = buildString {
    var duration = Duration.between(startInclusive, endExclusive)

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
            val hoursPart = duration.hoursPart.toString().padStart(2, '0')
            val minutesPart = duration.minutesPart.toString().padStart(2, '0')
            if (format.showSecond) {
                val secondsPart = duration.secondsPart.toString().padStart(2, '0')
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
        val seconds = duration.seconds
        append("${seconds.format(locale)}s ")
    }
}

fun differenceParseUniqueFormat(
    format: ChronometerFormat.ShowFlagType,
    locale: Locale,
    startInclusive: ZonedDateTime,
    endExclusive: ZonedDateTime
) = buildString {
    val duration = Duration.between(startInclusive, endExclusive)
    when (format) {
        ChronometerFormat.ShowFlagType.SECOND -> {
            val years = duration.seconds
            append("${years.format(locale)}s")
        }

        ChronometerFormat.ShowFlagType.MINUTE -> {
            val months = duration.seconds / 60.0
            append("${months.format(locale)}m")
        }

        ChronometerFormat.ShowFlagType.HOUR -> {
            val days = duration.seconds / 60.0 / 60.0
            append("${days.format(locale)}h")
        }

        ChronometerFormat.ShowFlagType.DAY -> {
            val hours = duration.seconds / 60.0 / 60.0 / 24.0
            append("${hours.format(locale)}d")
        }

        ChronometerFormat.ShowFlagType.WEEK -> {
            val minutes = duration.seconds / 60.0 / 60.0 / 24.0 / 30.0 / 4
            append("${minutes.format(locale)}w")
        }

        ChronometerFormat.ShowFlagType.MONTH -> {
            val seconds = duration.seconds / 60.0 / 60.0 / 24.0 / 30.0
            append("${seconds.format(locale)}m")
        }

        ChronometerFormat.ShowFlagType.YEAR -> {
            val seconds = duration.seconds / 60.0 / 60.0 / 24.0 / 30.0 / 365.0
            append("${seconds.format(locale)}y")
        }
    }
}

val Duration.hoursPart: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        toHoursPart()
    } else {
        (toHours() % 24).toInt()
    }

val Duration.minutesPart: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        toMinutesPart()
    } else {
        (toMinutes() % 60).toInt()
    }

val Duration.secondsPart: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        toSecondsPart()
    } else {
        (seconds % 60).toInt()
    }