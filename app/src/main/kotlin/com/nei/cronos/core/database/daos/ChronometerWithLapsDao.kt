package com.nei.cronos.core.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nei.cronos.core.database.models.ChronometerWithLaps
import kotlinx.coroutines.flow.Flow

@Dao
interface ChronometerWithLapsDao {
    @Transaction
    @Query("SELECT * FROM chronometers")
    fun flowAll(): Flow<List<ChronometerWithLaps>>

    @Transaction
    @Query("SELECT * FROM chronometers WHERE id = :chronometerId")
    fun byId(chronometerId: Long): Flow<ChronometerWithLaps?>
}