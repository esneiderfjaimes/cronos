package com.nei.cronos.core.common

import com.nei.cronos.domain.models.ChronometerUi
import java.time.ZonedDateTime

fun genTimeRange(chronometer: ChronometerUi): Pair<ZonedDateTime, ZonedDateTime> = when {
    // state new and running
    chronometer.lastEvent == null -> chronometer.startDate to ZonedDateTime.now()
    // state paused
    chronometer.isPaused -> chronometer.startDate to chronometer.lastEvent.time
    // state resuming
    else -> chronometer.lastEvent.time to ZonedDateTime.now()
}