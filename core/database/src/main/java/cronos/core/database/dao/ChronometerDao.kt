package cronos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import cronos.core.database.embeddeds.ChronometerWithEvents
import cronos.core.database.embeddeds.ChronometerWithLastEvent
import cronos.core.database.models.ChronometerEntity
import cronos.core.model.ChronometerFormat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChronometerDao {
    // region Create

    @Insert
    suspend fun insertAll(vararg chronometers: ChronometerEntity) : List<Long>

    @Insert
    suspend fun insert(chronometers: ChronometerEntity): Long

    // endregion
    // region Read
    @Query("SELECT * FROM chronometers WHERE is_archived = false")
    fun chronometers(): Flow<List<ChronometerEntity>>

    @Transaction
    @Query("SELECT * FROM chronometers WHERE id = :chronometerId")
    fun chronometerById(chronometerId: Long): Flow<ChronometerEntity?>

    @Transaction
    @Query("SELECT * FROM chronometers WHERE id = :chronometerId")
    fun chronometerWithEventsById(chronometerId: Long): Flow<ChronometerWithEvents?>

    @Transaction
    @Query("SELECT * FROM chronometers WHERE id = :chronometerId")
    fun chronometerWithLastEventById(chronometerId: Long): Flow<ChronometerWithLastEvent?>

    // endregion
    // region Update

    @Query("UPDATE chronometers SET is_active = :isActive WHERE id = :chronometerId")
    suspend fun updateIsActive(chronometerId: Long, isActive: Boolean): Int

    @Query("UPDATE chronometers SET title = :label WHERE id = :chronometerId")
    suspend fun updateLabel(chronometerId: Long, label: String): Int

    @Query("UPDATE chronometers SET format = :format WHERE id = :chronometerId")
    suspend fun updateFormat(chronometerId: Long, format: ChronometerFormat): Int

    @Query("UPDATE chronometers SET is_archived = :isArchived WHERE id = :chronometerId")
    suspend fun updateIsArchived(chronometerId: Long, isArchived: Boolean): Int

    // endregion
    // region Delete

    @Delete
    suspend fun delete(chronometer: ChronometerEntity): Int

    // endregion
}