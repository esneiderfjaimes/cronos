package com.nei.cronos.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nei.cronos.core.database.models.ChronometerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChronometerDao {
    @Insert
    fun insertAll(vararg chronometers: ChronometerEntity)

    @Query("SELECT * FROM chronometer")
    fun flowAll(): Flow<List<ChronometerEntity>>

    @Query("SELECT * FROM chronometer WHERE id = :id")
    fun byId(id: Int): ChronometerEntity

    @Update
    fun update(chronometer: ChronometerEntity)

    @Delete
    fun delete(chronometer: ChronometerEntity)
}