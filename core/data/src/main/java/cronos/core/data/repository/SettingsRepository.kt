package cronos.core.data.repository

import cronos.core.model.DarkThemeConfig
import cronos.core.model.Ids
import cronos.core.model.SettingsState
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    /**
     * Stream of [SettingsState]
     */
    val settingsState: Flow<SettingsState>

    /**
     * Sets the desired dark theme config.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * Sets the preferred dynamic color config.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Updates the order sections ids
     */
    suspend fun updateOrderSectionsIds(orderSectionsIds: Ids)

}