package com.nei.cronos.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nei.cronos.core.database.models.ChronometerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChronometerDao {
    @Query("SELECT * FROM chronometer")
    fun flowAll(): Flow<List<ChronometerEntity>>

    @Insert
    fun insertAll(vararg chronometers: ChronometerEntity)

    @Delete
    fun delete(chronometer: ChronometerEntity)
}