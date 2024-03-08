package cronos.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import cronos.core.database.models.EventEntity

@Dao
interface EventDao {
    @Insert
    suspend fun insert(event: EventEntity)
}