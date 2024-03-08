package com.nei.cronos.core.data

import com.nei.cronos.core.common.result.executeToResult
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.LapDao
import com.nei.cronos.core.database.daos.SectionDao
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.ChronometerWithLaps
import com.nei.cronos.core.database.models.LapEntity
import com.nei.cronos.core.database.models.SectionEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val sectionDao: SectionDao,
    private val chronometerDao: ChronometerDao,
    private val lapsDao: LapDao,
) : LocalRepository {
    // region section

    override suspend fun insertSection(section: SectionEntity) {
        executeToResult { sectionDao.insert(section) }
    }

    override fun sections() = sectionDao.sections()

    override fun sectionsWithChronometers() = sectionDao.sectionsWithChronometers()

    override fun sectionsWithChronometerById(sectionId: Long) =
        sectionDao.sectionsWithChronometerById(sectionId)

    // endregion
    // region chronometer

    override suspend fun insertChronometer(vararg chronometer: ChronometerEntity) {
        executeToResult { chronometerDao.insertAll(*chronometer) }
    }

    override fun chronometers() = chronometerDao.chronometers()

    override suspend fun updateChronometer(chronometer: ChronometerEntity) {
        executeToResult { chronometerDao.update(chronometer) }
    }

    override fun chronometerWithLapsById(id: Long): Flow<ChronometerWithLaps?> {
        return chronometerDao.chronometerWithLapsById(id)
    }

    // endregion
    // region lap

    override suspend fun registerLapIn(chronometer: ChronometerEntity) {
        executeToResult {  // create new lap
            lapsDao.insert(LapEntity(chronometerId = chronometer.id))
            // reset fromDate to now
            chronometerDao.update(chronometer.copy(fromDate = ZonedDateTime.now()))
        }
    }
    // endregion
}