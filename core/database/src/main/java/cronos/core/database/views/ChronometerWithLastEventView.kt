package cronos.core.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.PrimaryKey
import cronos.core.database.models.SectionEntity
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType
import java.time.ZonedDateTime

@DatabaseView(
    value = "SELECT " +
            "c.id as c_id," +
            "c.title as c_title, " +
            "c.created_at as c_created_at, " +
            "c.start_date as c_start_date, " +
            "c.format as c_format, " +
            "c.section_id as c_section_id, " +
            "c.is_active as c_is_active, " +
            "c.is_archived as c_is_archived, " +
            "e.id as last_event_id, " +
            "e.time as last_event_time, " +
            "e.type as last_event_type " +
            "FROM chronometers AS c " +
            "LEFT JOIN last_event_ref AS lef ON c.id = lef.chronometer_id " +
            "LEFT JOIN events AS e ON e.id = lef.last_event_id"
    ,
    viewName = "chronometer_with_last_event"
)
data class ChronometerWithLastEventView(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "c_id")
    var id: Long = 0,
    @ColumnInfo(name = "c_title")
    val title: String,
    @ColumnInfo(name = "c_created_at")
    val createdAt: ZonedDateTime,
    @ColumnInfo(name = "c_start_date")
    val startDate: ZonedDateTime,
    @ColumnInfo(name = "c_format")
    val format: ChronometerFormat = ChronometerFormat.DefaultFormat,
    @ColumnInfo(name = "c_section_id")
    val sectionId: Long = SectionEntity.NONE_SECTION_ID,
    @ColumnInfo(name = "c_is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "c_is_archived")
    val isArchived: Boolean = false,
    @Embedded(prefix = "last_event_")
    val lastEvent: LastEventView?
) {
    data class LastEventView(
        @ColumnInfo(name = "id")
        var id: Long = 0,
        @ColumnInfo(name = "time")
        val time: ZonedDateTime = ZonedDateTime.now(),
        @ColumnInfo(name = "type")
        val type: EventType
    )
}
