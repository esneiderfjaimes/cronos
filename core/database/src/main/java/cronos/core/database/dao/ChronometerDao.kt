package cronos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import cronos.core.database.models.ChronometerEntity
import cronos.core.database.embeddeds.ChronometerWithEvents
import cronos.core.database.embeddeds.ChronometerWithLastEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface ChronometerDao {
    @Insert
    suspend fun insertAll(vararg chronometers: ChronometerEntity)

    @Insert
    fun insert(chronometers: ChronometerEntity)

    @Query("SELECT * FROM chronometers WHERE is_archived = false")
    fun chronometers(): Flow<List<ChronometerEntity>>

    @Update
    suspend fun update(chronometer: ChronometerEntity)

    @Query("UPDATE chronometers SET is_archived = :isArchived WHERE id = :chronometerId")
    suspend fun updateIsArchived(chronometerId: Long, isArchived: Boolean) : Int

    @Delete
    suspend fun delete(chronometer: ChronometerEntity)

    @Transaction
    @Query("SELECT * FROM chronometers WHERE id = :chronometerId")
    fun chronometerWithEventsById(chronometerId: Long): Flow<ChronometerWithEvents?>

    @Transaction
    @Query("SELECT * FROM chronometers WHERE id = :chronometerId")
    fun chronometerWithLastEventById(chronometerId: Long): Flow<ChronometerWithLastEvent?>
}