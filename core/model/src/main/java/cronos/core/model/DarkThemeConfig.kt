package cronos.core.model

enum class DarkThemeConfig {
    FOLLOW_SYSTEM, LIGHT, DARK;

    companion object {
        fun fromInt(value: Int?): DarkThemeConfig = when (value) {
            0 -> FOLLOW_SYSTEM
            1 -> LIGHT
            2 -> DARK
            else -> FOLLOW_SYSTEM
        }
    }
}
