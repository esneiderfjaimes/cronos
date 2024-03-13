package cronos.core.data.repository

import cronos.core.datastore.SettingsRepositoryDataSource
import cronos.core.model.DarkThemeConfig
import cronos.core.model.Ids
import cronos.core.model.SettingsState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalSettingsRepository @Inject constructor(
    private val source: SettingsRepositoryDataSource
) : SettingsRepository {
    override val settingsState: Flow<SettingsState>
        get() = source.settingsState

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        source.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        source.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun updateOrderSectionsIds(orderSectionsIds: Ids) {
        source.updateOrderSectionsIds(orderSectionsIds)
    }
}