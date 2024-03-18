package cronos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import cronos.core.database.embeddeds.ChronometerWithEvents
import cronos.core.database.embeddeds.ChronometerWithLastEvent
import cronos.core.database.models.ChronometerEntity
import cronos.core.database.models.EventEntity
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface ChronometerDao {
    // region Create

    @Insert
    suspend fun insertAll(vararg chronometers: ChronometerEntity): List<Long>

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

    @Query(
        "UPDATE chronometers SET is_active = :isActive, last_time_running = :lastTimeRunning " +
                "WHERE id = :chronometerId"
    )
    suspend fun updateIsActiveAndLastTimeRunning(
        chronometerId: Long,
        isActive: Boolean,
        lastTimeRunning: ZonedDateTime
    ): Int

    @Transaction
    suspend fun registerEventIn(
        eventDao: EventDao,
        chronometer: ChronometerEntity,
        eventType: EventType
    ) {
        val eventEntity = EventEntity(
            chronometerId = chronometer.id,
            type = eventType
        )

        eventDao.insert(eventEntity)

        val isActive = when (eventType) {
            EventType.STOP -> false
            EventType.RESTART -> true
            EventType.LAP -> chronometer.isActive
        }

        if (eventType == EventType.STOP) {
            updateIsActive(
                chronometerId = chronometer.id,
                isActive = false,
            )
        } else {
            // reset fromDate to now
            updateIsActiveAndLastTimeRunning(
                chronometerId = chronometer.id,
                isActive = isActive,
                lastTimeRunning = ZonedDateTime.now()
            )
        }
    }

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