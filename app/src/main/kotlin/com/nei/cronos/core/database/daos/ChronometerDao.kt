package com.nei.cronos.core.database.daos

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
    suspend fun insertAll(vararg chronometers: ChronometerEntity)

    @Query("SELECT * FROM chronometers WHERE is_archived = false")
    fun flowAll(): Flow<List<ChronometerEntity>>

    @Query("SELECT * FROM chronometers WHERE id = :id")
    suspend fun byId(id: Long): ChronometerEntity?

    @Update
    suspend fun update(chronometer: ChronometerEntity)

    @Delete
    suspend fun delete(chronometer: ChronometerEntity)
}