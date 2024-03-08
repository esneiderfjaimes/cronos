package cronos.core.model

enum class EventType(val id: Byte) {
    LAP(0),
    STOP(1),
    RESTART(2);

    companion object {
        private val dir by lazy { entries.associateBy { it.id } }

        fun fromId(id: Byte): EventType {
            return dir[id] ?: throw IllegalArgumentException("Invalid event type: $id")
        }
    }
}