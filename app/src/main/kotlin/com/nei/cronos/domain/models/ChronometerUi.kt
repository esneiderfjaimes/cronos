package com.nei.cronos.domain.models

import androidx.compose.runtime.Stable
import com.nei.cronos.core.database.models.ChronometerFormat
import com.nei.cronos.core.database.models.SectionEntity
import java.time.ZoneId
import java.time.ZonedDateTime

@Stable
data class ChronometerUi(
    var id: Long = 0,
    val title: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
    val startDate: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
    val fromDate: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
    val format: ChronometerFormat = ChronometerFormat.DefaultFormat,
    val sectionId : Long = SectionEntity.NONE_SECTION_ID,
    val isActive: Boolean = true,
    val isArchived: Boolean = false,
)
