package cronos.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cronos.core.model.DarkThemeConfig
import cronos.core.model.Ids
import cronos.core.model.IdsParser
import cronos.core.model.SettingsState
import cronos.core.model.compact
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore: DataStore<Preferences> = context.settingsDataStore

    val settingsState: Flow<SettingsState>
        get() = dataStore.data.map { preferences ->
            val orderSectionIds =
                preferences[ORDER_SECTION_IDS]?.let { IdsParser.from(it) } ?: emptyList()

            SettingsState(
                orderSectionIds = orderSectionIds,
                useDynamicColor = preferences[ORDER_USE_DYNAMIC_COLOR] ?: true,
                darkThemeConfig = DarkThemeConfig.fromInt(preferences[ORDER_DARK_THEME])
            )
        }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataStore.edit {
            it[ORDER_DARK_THEME] = darkThemeConfig.ordinal
        }
    }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        dataStore.edit {
            it[ORDER_USE_DYNAMIC_COLOR] = useDynamicColor
        }
    }

    suspend fun updateOrderSectionsIds(orderSectionsIds: Ids) {
        dataStore.edit {
            it[ORDER_SECTION_IDS] = orderSectionsIds.compact()
        }
    }

    companion object {
        private const val NAME = "settings"
        private val Context.settingsDataStore by preferencesDataStore(NAME)
        private val ORDER_SECTION_IDS = stringPreferencesKey("order_section_ids")
        private val ORDER_USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        private val ORDER_DARK_THEME = intPreferencesKey("dark_theme_config")
    }
}