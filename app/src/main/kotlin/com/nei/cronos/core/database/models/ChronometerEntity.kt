package com.nei.cronos.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(tableName = "chronometers")
data class ChronometerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
    @ColumnInfo(name = "start_date")
    val startDate: ZonedDateTime,
    // mutable, represents last lap time
    @ColumnInfo(name = "from_date")
    val fromDate: ZonedDateTime,
    @ColumnInfo(name = "format")
    val format: ChronometerFormat = ChronometerFormat.DefaultFormat,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean = false,
) {
    constructor(
        title: String,
        allDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
        format: ChronometerFormat = ChronometerFormat.DefaultFormat
    ) : this(
        title = title,
        fromDate = allDateTime,
        startDate = allDateTime,
        format = format
    )
}
