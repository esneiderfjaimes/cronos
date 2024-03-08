package cronos.core.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    viewName = "last_event_ref",
    value = "SELECT chronometer_id, MAX(id) AS last_event_id FROM events GROUP BY chronometer_id"
)
data class LastEventRefView(
    @ColumnInfo(name = "chronometer_id")
    val chronometerId: Long,
    @ColumnInfo(name = "last_event_id")
    val lastEventId: Long
)
