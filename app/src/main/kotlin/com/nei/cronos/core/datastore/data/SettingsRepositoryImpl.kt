package com.nei.cronos.core.datastore.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nei.cronos.core.datastore.domain.SettingsRepository
import com.nei.cronos.core.datastore.domain.model.SettingsState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class SettingsRepositoryImpl(
    private val context: Context,
    private val settingsDataStore: DataStore<Preferences> = context.settingsDataStore
) : SettingsRepository {
    override suspend fun getSettings() = getSettingsFlow().first()

    override fun getSettingsFlow() = settingsDataStore.data.map { preferences ->
        SettingsState(
            orderSectionIds = preferences[ORDER_SECTION_IDS]
                ?.split(",")?.map { it.toLong() } ?: emptyList()
        )
    }

    override suspend fun setOrderSectionIds(orderSectionIds: List<Long>) {
        settingsDataStore.edit {
            it[ORDER_SECTION_IDS] = orderSectionIds.joinToString(",")
        }
    }

    companion object {
        private const val NAME = "settings"
        private val Context.settingsDataStore by preferencesDataStore(NAME)
        private val ORDER_SECTION_IDS = stringPreferencesKey("order_section_ids")
    }
}