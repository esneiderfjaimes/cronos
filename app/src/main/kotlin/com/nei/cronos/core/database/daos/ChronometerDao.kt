package com.nei.cronos.core.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.ChronometerWithLaps
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

    @Delete
    suspend fun delete(chronometer: ChronometerEntity)

    @Transaction
    @Query("SELECT * FROM chronometers WHERE id = :chronometerId")
    fun chronometerWithLapsById(chronometerId: Long): Flow<ChronometerWithLaps?>
}