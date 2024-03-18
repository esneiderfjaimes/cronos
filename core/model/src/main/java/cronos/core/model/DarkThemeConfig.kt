package cronos.core.model

enum class DarkThemeConfig {
    OFF,
    ON,
    SYSTEM;

    companion object {
        fun fromInt(value: Int?): DarkThemeConfig = when (value) {
            0 -> OFF
            1 -> ON
            else -> SYSTEM
        }
    }
}
