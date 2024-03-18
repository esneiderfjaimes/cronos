package com.nei.cronos.core.common

import com.nei.cronos.domain.models.ChronometerUi
import cronos.core.model.EventType
import java.time.ZonedDateTime

fun genTimeRange(chronometer: ChronometerUi): Pair<ZonedDateTime, ZonedDateTime> = when {
    // state new and running
    chronometer.lastEvent == null -> chronometer.createdAt to ZonedDateTime.now()
    // state paused
    chronometer.lastEvent.type == EventType.STOP -> chronometer.lastTimeRunning to chronometer.lastEvent.time
    // state resuming
    // state post new lap
    else -> chronometer.lastEvent.time to ZonedDateTime.now()
}