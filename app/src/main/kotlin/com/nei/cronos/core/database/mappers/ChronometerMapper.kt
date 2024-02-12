package com.nei.cronos.core.database.mappers

import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.domain.models.ChronometerUi

fun ChronometerEntity.toUi() = ChronometerUi(
    id = id,
    title = title,
    createdAt = createdAt,
    startDate = startDate,
    fromDate = fromDate,
    format = format,
    sectionId = sectionId,
    isActive = isActive,
    isArchived = isArchived
)

fun ChronometerUi.toDomain() = ChronometerEntity(
    id = id,
    title = title,
    createdAt = createdAt,
    startDate = startDate,
    fromDate = fromDate,
    format = format,
    sectionId = sectionId,
    isActive = isActive,
    isArchived = isArchived
)