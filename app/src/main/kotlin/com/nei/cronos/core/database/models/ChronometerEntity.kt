package com.nei.cronos.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "chronometer")
data class ChronometerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "from_date")
    val fromDate: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "format")
    val format: ChronometerFormat = ChronometerFormat.DefaultFormat,
)