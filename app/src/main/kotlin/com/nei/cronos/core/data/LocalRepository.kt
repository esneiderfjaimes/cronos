package com.nei.cronos.core.data

import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.ChronometerWithLaps
import com.nei.cronos.core.database.models.SectionEntity
import com.nei.cronos.core.database.models.SectionWithChronometers
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    // region section
    suspend fun insertSection(section: SectionEntity)

    fun sections(): Flow<List<SectionEntity>>

    fun sectionsWithChronometers(): Flow<List<SectionWithChronometers>>

    fun sectionsWithChronometerById(sectionId: Long): Flow<SectionWithChronometers?>

    // endregion
    // region chronometer

    fun chronometers(): Flow<List<ChronometerEntity>>

    suspend fun insertChronometer(vararg chronometer: ChronometerEntity)

    suspend fun updateChronometer(chronometer: ChronometerEntity)

    suspend fun registerLapIn(chronometer: ChronometerEntity)

    fun chronometerWithLapsById(id: Long): Flow<ChronometerWithLaps?>

    // endregion
}