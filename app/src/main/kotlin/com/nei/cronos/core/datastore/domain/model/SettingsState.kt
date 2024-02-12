package com.nei.cronos.core.datastore.domain.model

import androidx.compose.runtime.Stable

@Stable
data class SettingsState(
    val orderSectionIds : List<Long> = emptyList()
)
