package com.nei.cronos.core.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class SectionWithChronometers(
    @Embedded val section: SectionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "section_id"
    )
    val chronometers: List<ChronometerEntity>
)
