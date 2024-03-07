package com.nei.cronos.core.database.converters

import androidx.room.TypeConverter
import com.nei.cronos.core.model.EventType

class EventTypeConverter {
    @TypeConverter
    fun eventTypeToInt(eventType: EventType) = eventType.id

    @TypeConverter
    fun intToEventType(idEvent: Int) = EventType.fromId(idEvent)
}