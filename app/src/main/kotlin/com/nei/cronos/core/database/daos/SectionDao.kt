package com.nei.cronos.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.nei.cronos.core.database.models.SectionEntity
import com.nei.cronos.core.database.models.SectionWithChronometers
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionDao {
    @Insert
    fun insert(section: SectionEntity)

    @Query("SELECT * FROM sections")
    fun sections(): Flow<List<SectionEntity>>

    @Transaction
    @Query("SELECT * FROM sections")
    fun sectionsWithChronometers(): Flow<List<SectionWithChronometers>>

    @Transaction
    @Query("SELECT * FROM sections WHERE id = :sectionId")
    fun sectionsWithChronometerById(sectionId: Long): Flow<SectionWithChronometers?>
}