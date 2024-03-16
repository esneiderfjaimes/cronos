package cronos.core.data.repository

import cronos.core.data.utils.executeToResult
import cronos.core.database.dao.ChronometerDao
import cronos.core.database.dao.EventDao
import cronos.core.database.dao.SectionDao
import cronos.core.database.embeddeds.ChronometerWithEvents
import cronos.core.database.embeddeds.ChronometerWithLastEvent
import cronos.core.database.models.ChronometerEntity
import cronos.core.database.models.EventEntity
import cronos.core.database.models.SectionEntity
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType
import kotlinx.coroutines.flow.Flow
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

    // endregion
    // region chronometer

    override suspend fun insertChronometer(vararg chronometer: ChronometerEntity) {
        executeToResult { chronometerDao.insertAll(*chronometer) }
    }

    override fun chronometers() = chronometerDao.chronometers()

    override fun chronometerWithEventsById(id: Long): Flow<ChronometerWithEvents?> {
        return chronometerDao.chronometerWithEventsById(id)
    }

    override fun chronometerWithLastEventById(id: Long): Flow<ChronometerWithLastEvent?> {
        return chronometerDao.chronometerWithLastEventById(id)
    }

    override suspend fun updateChronometerIsActive(id: Long, isArchived: Boolean) =
        executeToResult {
            chronometerDao.updateIsArchived(id, isArchived)
        }

    override suspend fun updateChronometerLabel(chronometerId: Long, label: String) {
        executeToResult { chronometerDao.updateLabel(chronometerId, label) }
    }

    override suspend fun updateChronometerFormat(chronometerId: Long, format: ChronometerFormat) {
        executeToResult { chronometerDao.updateFormat(chronometerId, format) }
    }

    // endregion
    // region lap

    override suspend fun registerEventIn(
        chronometer: ChronometerEntity,
        eventType: EventType
    ) {
        executeToResult {  // create new lap
            lapsDao.insert(
                EventEntity(
                    chronometerId = chronometer.id,
                    type = eventType
                )
            )

            val isActive = when (eventType) {
                EventType.STOP -> false
                EventType.RESTART -> true
                EventType.LAP -> chronometer.isActive
            }

            // reset fromDate to now
            chronometerDao.updateIsActive(chronometer.id, isActive)
        }
    }

    override fun chronometerById(id: Long): Flow<ChronometerEntity?> {
        return chronometerDao.chronometerById(id)
    }
    // endregion
}