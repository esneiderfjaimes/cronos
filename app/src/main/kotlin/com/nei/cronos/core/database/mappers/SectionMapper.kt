package com.nei.cronos.core.database.mappers

import com.nei.cronos.core.database.embeddeds.SectionWithChronometers
import com.nei.cronos.domain.models.SectionUi

fun SectionWithChronometers.toUi() = SectionUi(
    id = section.id,
    name = section.name,
    orderChronometerIds = section.orderChronometerIds,
    chronometers = chronometers.map { it.toUi() },
)