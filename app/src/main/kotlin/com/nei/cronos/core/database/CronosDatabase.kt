package com.nei.cronos.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nei.cronos.core.database.converters.ChronometerFormatConverter
import com.nei.cronos.core.database.converters.TotalDateTimeConverter
import com.nei.cronos.core.database.models.ChronometerEntity

@Database(version = 1, entities = [ChronometerEntity::class])
@TypeConverters(TotalDateTimeConverter::class, ChronometerFormatConverter::class)
abstract class CronosDatabase : RoomDatabase() {
    abstract fun chronometerDao(): ChronometerDao

    companion object {

        private const val DATABASE_NAME = "cronos_database"

        @Synchronized
        fun getDatabase(context: Context) = Room.databaseBuilder(
            context,
            CronosDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}