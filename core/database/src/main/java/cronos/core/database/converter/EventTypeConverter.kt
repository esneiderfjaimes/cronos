package cronos.core.database.converter

import androidx.room.TypeConverter
import cronos.core.model.EventType

class EventTypeConverter {

    @TypeConverter
    fun eventTypeToInt(eventType: EventType) = eventType.id

    @TypeConverter
    fun intToEventType(idEvent: Byte) = EventType.fromId(idEvent)

}