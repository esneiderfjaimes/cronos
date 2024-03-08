package cronos.core.database.embeddeds

import androidx.room.Embedded
import androidx.room.Relation
import cronos.core.database.models.ChronometerEntity
import cronos.core.database.models.EventEntity

data class ChronometerWithLastEvent(
    @Embedded val chronometer: ChronometerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "chronometer_id"
    )
    val lastEvent: EventEntity?
)