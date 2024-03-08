package com.nei.cronos.utils

import java.time.Duration

fun Duration.toWeeks(): Long = if (toDays() < 7) 0 else toDays() / 7

fun Duration.toMonths(): Long = if (toDays() < 30) 0 else toDays() / 30

fun Duration.toYears(): Long = if (toDays() < 365) 0 else toDays() / 365

fun Duration.minusWeeks(weeks: Long): Duration = minusDays(weeks * 7)

fun Duration.minusMonths(months: Long): Duration = minusDays(months * 30)

fun Duration.minusYears(years: Long): Duration = minusDays(years * 365)