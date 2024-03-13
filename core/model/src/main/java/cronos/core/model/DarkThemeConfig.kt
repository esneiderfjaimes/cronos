package cronos.core.model

enum class DarkThemeConfig {
    SYSTEM, OFF, ON;

    companion object {
        fun fromInt(value: Int?): DarkThemeConfig = when (value) {
            0 -> SYSTEM
            1 -> OFF
            2 -> ON
            else -> SYSTEM
        }
    }
}
