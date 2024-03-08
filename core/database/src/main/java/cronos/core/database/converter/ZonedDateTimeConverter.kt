package cronos.core.database.converter

import androidx.room.TypeConverter
import java.time.ZonedDateTime

class ZonedDateTimeConverter {

    @TypeConverter
    fun timeToString(time: ZonedDateTime): String {
        return time.toString()
    }

    @TypeConverter
    fun stringToTime(parse: String): ZonedDateTime {
        return ZonedDateTime.parse(parse)
    }

}