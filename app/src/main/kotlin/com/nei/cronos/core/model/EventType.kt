package com.nei.cronos.core.model

enum class EventType(val id: Int) {
    PAUSE(0),
    RESUME(1),
    LAP(3);

    companion object {
        private val dir by lazy { entries.associateBy { it.id } }

        fun fromId(id: Int): EventType {
            return dir[id] ?: throw IllegalArgumentException("Invalid event type: $id")
        }
    }
}