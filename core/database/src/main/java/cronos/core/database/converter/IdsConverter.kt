package cronos.core.database.converter

import androidx.room.TypeConverter
import cronos.core.model.Ids
import cronos.core.model.IdsParser
import cronos.core.model.compact

class IdsConverter {

    @TypeConverter
    fun stringToIds(ids: String): Ids {
        return IdsParser.from(ids)
    }

    @TypeConverter
    fun idsToString(ids: Ids): String {
        return ids.compact()
    }

}