package cronos.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import cronos.core.model.EventType
import java.time.ZonedDateTime

@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = ChronometerEntity::class,
            parentColumns = ["id"],
            childColumns = ["chronometer_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id"]),
        Index(value = ["chronometer_id"]),
    ]
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "chronometer_id")
    val chronometerId: Long,
    @ColumnInfo(name = "time")
    val time: ZonedDateTime = ZonedDateTime.now(),
    @ColumnInfo(name = "type")
    val type: EventType
)