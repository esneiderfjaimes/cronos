package com.nei.cronos.domain.models

import androidx.compose.runtime.Stable
import cronos.core.database.models.EventEntity
import cronos.core.database.models.SectionEntity
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType
import java.time.ZonedDateTime

@Stable
data class ChronometerUi(
    var id: Long = 0,
    val title: String,
    val createdAt: ZonedDateTime,
    @Deprecated("same [created_at] field")
    val startDate: ZonedDateTime,
    val lastTimeRunning: ZonedDateTime,
    val format: ChronometerFormat = ChronometerFormat.DefaultFormat,
    val sectionId: Long = SectionEntity.NONE_SECTION_ID,
    @Deprecated("Use last event to check if chronometer is [EventType.STOP]")
    val isActive: Boolean = true,
    val isArchived: Boolean = false,
    val lastEvent: EventEntity? = null
) {
    val isPaused: Boolean = (lastEvent?.type == EventType.STOP)
}
