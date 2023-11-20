package com.nei.cronos.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "chronometer")
data class ChronometerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    @ColumnInfo(name = "from_date")
    val fromDate: ZonedDateTime = ZonedDateTime.now(),
    @ColumnInfo(name = "format")
    val format: ChronometerFormat = ChronometerFormat.DefaultFormat,
)