package com.nei.cronos.core.data

import com.nei.cronos.core.common.result.executeToResult
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.EventDao
import com.nei.cronos.core.database.daos.SectionDao
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.embeddeds.ChronometerWithEvents
import com.nei.cronos.core.database.embeddeds.ChronometerWithLastEvent
import com.nei.cronos.core.database.models.EventEntity
import com.nei.cronos.core.database.models.SectionEntity
import com.nei.cronos.core.model.EventType
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val sectionDao: SectionDao,
    private val chronometerDao: ChronometerDao,
    private val lapsDao: EventDao,
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

    override fun chronometerWithEventsById(id: Long): Flow<ChronometerWithEvents?> {
        return chronometerDao.chronometerWithEventsById(id)
    }

    override fun chronometerWithLastEventById(id: Long): Flow<ChronometerWithLastEvent?> {
        return chronometerDao.chronometerWithLastEventById(id)
    }

    override suspend fun updateChronometerIsActive(id: Long, isArchived: Boolean) = executeToResult {
        chronometerDao.updateIsArchived(id, isArchived)
    }

    // endregion
    // region lap

    override suspend fun registerEventIn(chronometer: ChronometerEntity, eventType: EventType) {
        executeToResult {  // create new lap
            lapsDao.insert(EventEntity(chronometerId = chronometer.id, type = eventType))

            val isActive = when (eventType) {
                EventType.PAUSE -> false
                EventType.RESUME -> true
                EventType.LAP -> chronometer.isActive
            }

            val fromDate = if (eventType == EventType.LAP) ZonedDateTime.now()
            else chronometer.fromDate

            // reset fromDate to now
            chronometerDao.update(
                chronometer.copy(
                    fromDate = fromDate,
                    isActive = isActive
                )
            )
        }
    }
    // endregion
}