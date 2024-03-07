package com.nei.cronos.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    object Schema1to2 : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            arrayOf(
                "ALTER TABLE laps RENAME TO events",
                "ALTER TABLE events RENAME COLUMN id TO event_id",
                "ALTER TABLE events RENAME COLUMN lap_time TO event_time",
                "ALTER TABLE events ADD COLUMN event_type INTEGER NOT NULL DEFAULT 3",
            ).forEach { query -> db.execSQL(query) }
        }
    }
}