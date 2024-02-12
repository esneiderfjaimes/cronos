package com.nei.cronos.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nei.cronos.core.database.converters.ChronometerFormatConverter
import com.nei.cronos.core.database.converters.OrderChronometerIdsConverter
import com.nei.cronos.core.database.converters.ZonedDateTimeConverter
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.LapDao
import com.nei.cronos.core.database.daos.SectionDao
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.LapEntity
import com.nei.cronos.core.database.models.SectionEntity
import com.nei.cronos.utils.Mocks

@Database(
    version = 1,
    entities = [
        SectionEntity::class,
        ChronometerEntity::class,
        LapEntity::class
    ],
    exportSchema = true
)
@TypeConverters(
    OrderChronometerIdsConverter::class,
    ZonedDateTimeConverter::class,
    ChronometerFormatConverter::class
)
abstract class CronosDatabase : RoomDatabase() {

    abstract fun sectionDao(): SectionDao

    abstract fun chronometerDao(): ChronometerDao

    abstract fun lapDao(): LapDao

    companion object {

        private const val DATABASE_NAME = "cronos_database"

        @Synchronized
        fun getDatabase(context: Context): CronosDatabase {
            return Room.databaseBuilder(
                context,
                CronosDatabase::class.java,
                DATABASE_NAME
            ).run {
                fallbackToDestructiveMigration()
                addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // insert default values
                        SectionEntity.onCreate(db)
                        ChronometerEntity.onDbCreate(db, Mocks.getFirstChronometer(context))
                    }
                })
                build()
            }
        }
    }
}