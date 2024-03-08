package com.nei.cronos.core.database.embeddeds

import androidx.room.Embedded
import androidx.room.Relation
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.SectionEntity

data class SectionWithChronometers(
    @Embedded val section: SectionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "section_id"
    )
    val chronometers: List<ChronometerEntity>
)
