package com.nei.cronos.core.data

import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.ChronometerWithLaps
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    fun flowAllChronometers(): Flow<List<ChronometerEntity>>

    suspend fun insertChronometer(vararg chronometer: ChronometerEntity)

    suspend fun updateChronometer(chronometer: ChronometerEntity)

    suspend fun registerLapIn(chronometer: ChronometerEntity)
    fun flowChronometerWithLapsById(id: Long): Flow<ChronometerWithLaps?>
}