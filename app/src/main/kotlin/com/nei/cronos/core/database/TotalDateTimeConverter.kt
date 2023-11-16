package com.nei.cronos.core.database

import androidx.room.TypeConverter
import java.time.LocalDateTime

class TotalDateTimeConverter {
    @TypeConverter
    fun timeToString(time: LocalDateTime): String {
        return time.toString()
    }

    @TypeConverter
    fun stringToTime(parse: String): LocalDateTime {
        return LocalDateTime.parse(parse)
    }
}