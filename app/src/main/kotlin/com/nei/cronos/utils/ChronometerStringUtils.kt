package com.nei.cronos.utils

import android.os.Build
import java.time.Duration
import java.time.LocalDateTime

object ChronometerStringUtils {
    fun LocalDateTime.differenceParse(): String {
        val now = LocalDateTime.now()
        val diff = Duration.between(this, now)
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) "00:00:00"
        else {
            buildString {
                val daysPart = diff.toDaysPart()
                if (daysPart > 0) append("${daysPart}d ")
                val hoursPart = diff.toHoursPart().toString().padStart(2, '0')
                val minutesPart = diff.toMinutesPart().toString().padStart(2, '0')
                val secondsPart = diff.toSecondsPart().toString().padStart(2, '0')
                append("$hoursPart:$minutesPart:$secondsPart")
            }
        }
    }
}