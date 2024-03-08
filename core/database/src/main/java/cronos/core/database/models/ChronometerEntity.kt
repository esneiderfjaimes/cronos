package cronos.core.database.models

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.sqlite.db.SupportSQLiteDatabase
import cronos.core.database.converter.ChronometerFormatConverter
import cronos.core.database.converter.ZonedDateTimeConverter
import cronos.core.model.ChronometerFormat
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(
    tableName = "chronometers",
    foreignKeys = [
        ForeignKey(
            entity = SectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["section_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["section_id"]),
    ]
)
data class ChronometerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: ZonedDateTime,
    @ColumnInfo(name = "start_date")
    val startDate: ZonedDateTime,
    // mutable, represents last event time
    @ColumnInfo(name = "from_date")
    val fromDate: ZonedDateTime,
    @ColumnInfo(name = "format")
    val format: ChronometerFormat = ChronometerFormat.DefaultFormat,
    @ColumnInfo(name = "section_id")
    val sectionId: Long = SectionEntity.NONE_SECTION_ID,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean = false,
) {
    constructor(
        title: String,
        allDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
        format: ChronometerFormat = ChronometerFormat.DefaultFormat,
        sectionId: Long = SectionEntity.NONE_SECTION_ID,
        isActive: Boolean = true,
        isArchived: Boolean = false
    ) : this(
        title = title,
        createdAt = ZonedDateTime.now(ZoneId.systemDefault()),
        fromDate = allDateTime,
        startDate = allDateTime,
        format = format,
        sectionId = sectionId,
        isActive = isActive,
        isArchived = isArchived
    )

    companion object {
        fun onDbCreate(db: SupportSQLiteDatabase, firstChronometer: ChronometerEntity) {
            val timeConverter = ZonedDateTimeConverter()
            val formatConverter = ChronometerFormatConverter()
            with(firstChronometer) {
                db.insert(
                    "chronometers",
                    conflictAlgorithm = SQLiteDatabase.CONFLICT_REPLACE,
                    values = ContentValues().apply {
                        put("title", title)
                        put("created_at", timeConverter.timeToString(createdAt))
                        put("start_date", timeConverter.timeToString(startDate))
                        put("from_date", timeConverter.timeToString(fromDate))
                        put("format", formatConverter.formatToString(format))
                        put("section_id", sectionId)
                        put("is_active", isActive)
                        put("is_archived", isArchived)
                    }
                )
            }
        }
    }
}
