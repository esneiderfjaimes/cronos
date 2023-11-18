package com.nei.cronos.core.database.converters

import androidx.room.TypeConverter
import com.nei.cronos.core.database.models.ChronometerFormat

class ChronometerFormatConverter {
    @TypeConverter
    fun chronometerFormatToString(chronometerFormat: ChronometerFormat): Int {
        return chronometerFormat.toFlags()
    }

    @TypeConverter
    fun stringToChronometerFormat(flags: Int): ChronometerFormat {
        return ChronometerFormat.fromFlags(flags)
    }
}