package cronos.core.database.converter

import androidx.room.TypeConverter
import cronos.core.model.ChronometerFormat

class ChronometerFormatConverter {

    @TypeConverter
    fun formatToInt(chronometerFormat: ChronometerFormat): Int {
        return chronometerFormat.toFlags()
    }

    @TypeConverter
    fun intToFormat(flags: Int): ChronometerFormat {
        return ChronometerFormat.fromFlags(flags)
    }

}