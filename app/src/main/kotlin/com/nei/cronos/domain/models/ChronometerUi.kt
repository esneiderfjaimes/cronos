package com.nei.cronos.domain.models

import androidx.compose.runtime.Stable
import cronos.core.database.models.EventEntity
import cronos.core.database.models.SectionEntity
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType
import java.time.ZoneId
import java.time.ZonedDateTime

@Stable
data class ChronometerUi(
    var id: Long = 0,
    val title: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
    val startDate: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
    val format: ChronometerFormat = ChronometerFormat.DefaultFormat,
    val sectionId: Long = SectionEntity.NONE_SECTION_ID,
    val isActive: Boolean = true,
    val isArchived: Boolean = false,
    val lastEvent: EventEntity? = null
) {
    val isPaused: Boolean =
        (!isActive && lastEvent?.type == EventType.STOP)
}
