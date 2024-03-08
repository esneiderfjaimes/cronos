package com.nei.cronos.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(
    tableName = "laps",
    foreignKeys = [
        ForeignKey(
            entity = ChronometerEntity::class,
            parentColumns = ["id"],
            childColumns = ["chronometer_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["chronometer_id"]),
    ]
)
data class LapEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "chronometer_id")
    val chronometerId: Long,
    @ColumnInfo(name = "lap_time")
    val lapTime: ZonedDateTime = ZonedDateTime.now()
)