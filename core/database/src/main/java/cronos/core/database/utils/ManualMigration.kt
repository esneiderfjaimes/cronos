package cronos.core.database.utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

open class ManualMigration(
    startVersion: Int, endVersion: Int, private vararg val queries: String
) : Migration(startVersion, endVersion) {
    override fun migrate(db: SupportSQLiteDatabase) {
        queries.forEach { query -> db.execSQL(query) }
    }
}