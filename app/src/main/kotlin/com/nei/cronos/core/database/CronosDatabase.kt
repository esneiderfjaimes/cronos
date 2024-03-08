package com.nei.cronos.core.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nei.cronos.core.database.converters.ChronometerFormatConverter
import com.nei.cronos.core.database.converters.EventTypeConverter
import com.nei.cronos.core.database.converters.OrderChronometerIdsConverter
import com.nei.cronos.core.database.converters.ZonedDateTimeConverter
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.EventDao
import com.nei.cronos.core.database.daos.SectionDao
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.EventEntity
import com.nei.cronos.core.database.models.SectionEntity
import com.nei.cronos.core.database.views.ChronometerWithLastEvent
import com.nei.cronos.utils.Mocks

@Database(
    version = 2,
    entities = [SectionEntity::class, ChronometerEntity::class, EventEntity::class],
    views = [ChronometerWithLastEvent::class],
    exportSchema = true,
)
@TypeConverters(
    OrderChronometerIdsConverter::class,
    ZonedDateTimeConverter::class,
    ChronometerFormatConverter::class,
    EventTypeConverter::class
)
abstract class CronosDatabase : RoomDatabase() {

    abstract fun sectionDao(): SectionDao

    abstract fun chronometerDao(): ChronometerDao

    abstract fun lapDao(): EventDao

    companion object {

        private const val DATABASE_NAME = "cronos_database"

        @Synchronized
        fun getDatabase(context: Context): CronosDatabase {
            return Room.databaseBuilder(
                context, CronosDatabase::class.java, DATABASE_NAME
            ).run {
                addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d(TAG, "callback onCreate called")

                        // insert default values
                        SectionEntity.onCreate(db)
                        ChronometerEntity.onDbCreate(db, Mocks.getFirstChronometer(context))
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        Log.d(TAG, "callback onDestructiveMigration called")
                    }
                })
                fallbackToDestructiveMigrationOnDowngrade()
                addMigrations(DatabaseMigrations.Schema1to2)
                build()
            }
        }

        private const val TAG = "CronosDatabase"
    }
}