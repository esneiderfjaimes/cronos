package com.nei.cronos.core.data

import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.ChronometerWithLapsDao
import com.nei.cronos.core.database.daos.LapDao
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.ChronometerWithLaps
import com.nei.cronos.core.database.models.LapEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val chronometerDao: ChronometerDao,
    private val lapsDao: LapDao,
    private val chronometerWithLapsDao: ChronometerWithLapsDao
) : LocalRepository {
    override fun flowAllChronometers(): Flow<List<ChronometerEntity>> {
        return chronometerDao.flowAll()
    }

    override suspend fun insertChronometer(vararg chronometer: ChronometerEntity) {
        return chronometerDao.insertAll(*chronometer)
    }

    override suspend fun updateChronometer(chronometer: ChronometerEntity) {
        chronometerDao.update(chronometer)
    }

    override suspend fun registerLapIn(chronometer: ChronometerEntity) {
        // create new lap
        lapsDao.insert(LapEntity(chronometerId = chronometer.id))
        // reset fromDate to now
        chronometerDao.update(chronometer.copy(fromDate = ZonedDateTime.now()))
    }

    override fun flowChronometerWithLapsById(id: Long): Flow<ChronometerWithLaps?> {
        return chronometerWithLapsDao.byId(id)
    }
}
