package com.nei.cronos.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import com.nei.cronos.core.database.models.EventEntity

@Dao
interface EventDao {
    @Insert
    suspend fun insert(event: EventEntity)
}