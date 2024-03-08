package com.nei.cronos.core.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded

@DatabaseView(
    viewName = "chronometer_with_last_event",
    value = "SELECT c.id AS id, c.title, c.created_at, c.start_date, c.from_date, c.format, " +
            "c.section_id, c.is_active, c.is_archived, e.event_id AS last_event_id, " +
            "e.event_time AS last_event_time, e.event_type AS last_event_type " +
            "FROM chronometers c LEFT JOIN (SELECT chronometer_id, MAX(event_time) AS maxEventTime " +
            "FROM events GROUP BY chronometer_id) latestEvent ON c.id = latestEvent.chronometer_id " +
            "LEFT JOIN events e ON latestEvent.chronometer_id = e.chronometer_id " +
            "AND latestEvent.maxEventTime = e.event_time"
)
data class ChronometerWithLastEvent(
    @ColumnInfo(name = "id")
    val chronometerId: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @ColumnInfo(name = "start_date")
    val startDate: String,
    @ColumnInfo(name = "from_date")
    val fromDate: String,
    @ColumnInfo(name = "format")
    val format: Int,
    @ColumnInfo(name = "section_id")
    val sectionId: Int,
    @ColumnInfo(name = "is_active")
    val isActive: Int,
    @ColumnInfo(name = "is_archived")
    val isArchived: Int,
    @Embedded val lastEvent: LastEvent?
) {
    data class LastEvent(
        @ColumnInfo(name = "last_event_id")
        val lastEventId: Long,
        @ColumnInfo(name = "last_event_time")
        val lastEventTime: String,
        @ColumnInfo(name = "last_event_type")
        val lastEventType: Int
    )
}
