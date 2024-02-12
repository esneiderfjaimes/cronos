package com.nei.cronos.core.database.mappers

import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.domain.models.ChronometerUi

fun ChronometerEntity.toUi() = ChronometerUi(
    id = id,
    title = title,
    startDate = startDate,
    fromDate = fromDate,
    format = format,
    isActive = isActive,
    isArchived = isArchived
)