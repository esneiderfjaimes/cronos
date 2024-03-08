package com.nei.cronos.core.database.models

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(
    tableName = "sections",
)
data class SectionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "order_chronometer_ids")
    val orderChronometerIds: List<Long>,
    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean = false,
) {
    companion object {
        const val NONE_SECTION_ID = -1L

        fun onCreate(db: SupportSQLiteDatabase) {
            db.insert(
                table = "sections",
                conflictAlgorithm = SQLiteDatabase.CONFLICT_REPLACE,
                values = ContentValues().apply {
                    put("id", NONE_SECTION_ID)
                    put("name", "default")
                    put("order_chronometer_ids", "")
                    put("is_archived", false)
                }
            )
        }
    }
}