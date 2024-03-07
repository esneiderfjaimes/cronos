package com.nei.cronos.core.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class ChronometerWithLaps(
    @Embedded val chronometer: ChronometerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "chronometer_id"
    )
    val events: List<EventEntity>
)
