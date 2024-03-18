package cronos.core.database

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec
import cronos.core.database.utils.ManualMigration
import cronos.core.model.EventType

object DatabaseMigrations {
    object Schema1to2 : ManualMigration(
        startVersion = 1,
        endVersion = 2,
        // Table Laps to Events
        "ALTER TABLE laps RENAME TO events",
        "ALTER TABLE events RENAME COLUMN lap_time TO time",
        "ALTER TABLE events ADD COLUMN type INTEGER NOT NULL DEFAULT ${EventType.LAP}",
        ///"CREATE VIEW `ChronometerWithLastEvent` AS SELECT c.id AS id, c.title, c.created_at, c.start_date, c.from_date, c.format, c.section_id, c.is_active, c.is_archived, e.event_id AS last_event_id, e.event_time AS last_event_time, e.event_type AS last_event_type FROM chronometers c LEFT JOIN (SELECT chronometer_id, MAX(event_time) AS maxEventTime FROM events GROUP BY chronometer_id) latestEvent ON c.id = latestEvent.chronometer_id LEFT JOIN events e ON latestEvent.chronometer_id = e.chronometer_id AND latestEvent.maxEventTime = e.event_time",
        // TODO("This migrations is not implemented yet. DB version 1 is not available in beta versions.")
    )

    @RenameColumn(
        tableName = "chronometers",
        fromColumnName = "from_date",
        toColumnName = "last_time_running",
    )
    class Schema2to3 : AutoMigrationSpec
}