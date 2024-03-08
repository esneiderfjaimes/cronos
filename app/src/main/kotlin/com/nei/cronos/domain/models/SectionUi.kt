package com.nei.cronos.domain.models

import androidx.compose.runtime.Stable

@Stable
data class SectionUi(
    val id: Long,
    val name: String,
    val orderChronometerIds: List<Long>,
    val chronometers: List<ChronometerUi>
)
