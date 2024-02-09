package com.nei.cronos.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nei.cronos.core.database.converters.ChronometerFormatConverter
import com.nei.cronos.core.database.converters.TotalDateTimeConverter
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.ChronometerWithLapsDao
import com.nei.cronos.core.database.daos.LapDao
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.database.models.LapEntity

@Database(version = 1, entities = [ChronometerEntity::class, LapEntity::class])
@TypeConverters(TotalDateTimeConverter::class, ChronometerFormatConverter::class)
abstract class CronosDatabase : RoomDatabase() {
    abstract fun chronometerDao(): ChronometerDao

    abstract fun lapDao(): LapDao

    abstract fun chronometerWithLapsDao(): ChronometerWithLapsDao

    companion object {

        private const val DATABASE_NAME = "cronos_database"

        @Synchronized
        fun getDatabase(context: Context) = Room.databaseBuilder(
            context,
            CronosDatabase::class.java,
            DATABASE_NAME
        ).run {
            fallbackToDestructiveMigration()
            build()
        }
    }
}