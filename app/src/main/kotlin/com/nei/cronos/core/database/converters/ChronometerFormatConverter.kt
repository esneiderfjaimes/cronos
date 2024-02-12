package com.nei.cronos.core.database.converters

import androidx.room.TypeConverter
import com.nei.cronos.core.database.models.ChronometerFormat

class ChronometerFormatConverter {
    @TypeConverter
    fun formatToString(chronometerFormat: ChronometerFormat): Int {
        return chronometerFormat.toFlags()
    }

    @TypeConverter
    fun stringToFormat(flags: Int): ChronometerFormat {
        return ChronometerFormat.fromFlags(flags)
    }
}