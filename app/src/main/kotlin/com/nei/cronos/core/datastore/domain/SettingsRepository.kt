package com.nei.cronos.core.datastore.domain

import com.nei.cronos.core.datastore.domain.model.SettingsState
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun getSettings(): SettingsState

    fun getSettingsFlow(): Flow<SettingsState>

    suspend fun setOrderSectionIds(orderSectionIds: List<Long>)
}