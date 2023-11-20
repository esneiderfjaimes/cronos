package com.nei.cronos.core.database.converters

import androidx.room.TypeConverter
import java.time.ZonedDateTime

class TotalDateTimeConverter {
    @TypeConverter
    fun timeToString(time: ZonedDateTime): String {
        return time.toString()
    }

    @TypeConverter
    fun stringToTime(parse: String): ZonedDateTime {
        return ZonedDateTime.parse(parse)
    }
}