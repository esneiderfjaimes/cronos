package com.nei.cronos.core.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration as MigrationRoom

object DatabaseMigrations {
    object Schema1to2 : Migration(1, 2, { db ->
        arrayOf(
            "ALTER TABLE laps RENAME TO events",
            "ALTER TABLE events RENAME COLUMN id TO event_id",
            "ALTER TABLE events RENAME COLUMN lap_time TO event_time",
            "ALTER TABLE events ADD COLUMN event_type INTEGER NOT NULL DEFAULT 3",
            "CREATE VIEW `ChronometerWithLastEvent` AS SELECT c.id AS id, c.title, c.created_at, c.start_date, c.from_date, c.format, c.section_id, c.is_active, c.is_archived, e.event_id AS last_event_id, e.event_time AS last_event_time, e.event_type AS last_event_type FROM chronometers c LEFT JOIN (SELECT chronometer_id, MAX(event_time) AS maxEventTime FROM events GROUP BY chronometer_id) latestEvent ON c.id = latestEvent.chronometer_id LEFT JOIN events e ON latestEvent.chronometer_id = e.chronometer_id AND latestEvent.maxEventTime = e.event_time",
        ).forEach { query -> db.execSQL(query) }
    })

    open class Migration(
        startVersion: Int, endVersion: Int, val scope: (db: SupportSQLiteDatabase) -> Unit
    ) : MigrationRoom(startVersion, endVersion) {
        override fun migrate(db: SupportSQLiteDatabase) = scope(db)
    }
}