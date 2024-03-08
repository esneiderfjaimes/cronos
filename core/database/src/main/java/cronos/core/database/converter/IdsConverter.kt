package cronos.core.database.converter

import androidx.room.TypeConverter
import cronos.core.model.Ids

class IdsConverter {

    @TypeConverter
    fun stringToIds(ids: String): Ids {
        if (ids.isBlank()) return emptyList()
        return ids.split(",")
            .takeIf { it.isNotEmpty() }
            ?.map { it.toLong() }
            ?: emptyList()
    }

    @TypeConverter
    fun idsToString(ids: Ids): String {
        return ids.joinToString(",")
    }

}