package com.nei.cronos.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import com.nei.cronos.core.database.models.LapEntity

@Dao
interface LapDao {
    @Insert
    suspend fun insert(lap: LapEntity)
}