package cronos.core.data.repository

import cronos.core.database.embeddeds.ChronometerWithEvents
import cronos.core.database.embeddeds.ChronometerWithLastEvent
import cronos.core.database.models.ChronometerEntity
import cronos.core.database.models.SectionEntity
import cronos.core.model.EventType
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    // region section
    suspend fun insertSection(section: SectionEntity)

    fun sections(): Flow<List<SectionEntity>>

    // endregion
    // region chronometer

    fun chronometers(): Flow<List<ChronometerEntity>>

    suspend fun insertChronometer(vararg chronometer: ChronometerEntity)

    suspend fun updateChronometer(chronometer: ChronometerEntity)

    suspend fun registerEventIn(chronometer: ChronometerEntity, eventType: EventType)

    fun chronometerWithEventsById(id: Long): Flow<ChronometerWithEvents?>

    fun chronometerWithLastEventById(id: Long): Flow<ChronometerWithLastEvent?>

    suspend fun updateChronometerIsActive(id: Long, isArchived: Boolean): Result<Int>

    // endregion
}