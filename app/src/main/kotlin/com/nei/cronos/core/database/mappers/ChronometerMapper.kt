package com.nei.cronos.core.database.mappers

import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.domain.models.SectionUi
import cronos.core.database.embeddeds.ChronometerWithLastEvent
import cronos.core.database.models.ChronometerEntity
import cronos.core.database.models.EventEntity
import cronos.core.database.models.SectionEntity
import cronos.core.database.views.ChronometerWithLastEventView
import cronos.core.database.views.SectionView
import cronos.core.database.views.SectionWithChronometers

fun ChronometerWithLastEvent.toUi() = chronometer.toUi(lastEvent)

fun ChronometerEntity.toUi(
    lastEvent: EventEntity?
) = ChronometerUi(
    id = id,
    title = title,
    createdAt = createdAt,
    startDate = startDate,
    format = format,
    sectionId = sectionId,
    isActive = isActive,
    isArchived = isArchived,
    lastEvent = lastEvent
)

fun ChronometerUi.toDomain() = ChronometerEntity(
    id = id,
    title = title,
    createdAt = createdAt,
    startDate = startDate,
    fromDate = startDate,
    format = format,
    sectionId = sectionId,
    isActive = isActive,
    isArchived = isArchived
)

fun SectionView.toUi() = SectionUi(
    id = section.id,
    name = section.name,
    orderChronometerIds = section.orderChronometerIds,
    chronometers = chronometers.map { it.toUi() }
)

fun ChronometerWithLastEventView.toUi() = ChronometerUi(
    id = id,
    title = title,
    createdAt = createdAt,
    startDate = startDate,
    format = format,
    sectionId = sectionId,
    isActive = isActive,
    isArchived = isArchived,
    lastEvent = lastEvent?.toUi(id)
)

fun ChronometerWithLastEventView.LastEventView.toUi(
    chronometerId: Long,
) = EventEntity(
    time = time,
    type = type,
    chronometerId = chronometerId
)


fun Map<SectionEntity, List<ChronometerWithLastEventView>>.toUi() = map { (section, chronometers) ->
    SectionUi(
        id = section.id,
        name = section.name,
        orderChronometerIds = section.orderChronometerIds,
        chronometers = chronometers.map { it.toUi() }
    )
}

fun SectionWithChronometers.toUi() = SectionUi(
    id = section.id,
    name = section.name,
    orderChronometerIds = section.orderChronometerIds,
    chronometers = chronometers.map { it.toUi() }
)