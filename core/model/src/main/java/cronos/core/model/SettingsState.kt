package cronos.core.model

data class SettingsState(
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val orderSectionIds: Ids = emptyList()
)
