package com.nei.cronos.core.database.embeddeds

import androidx.room.Embedded
import androidx.room.Relation
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.EventEntity

data class ChronometerWithEvents(
    @Embedded val chronometer: ChronometerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "chronometer_id"
    )
    val events: List<EventEntity>
)

