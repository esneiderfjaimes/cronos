package com.nei.cronos.core.di

import android.content.Context
import com.nei.cronos.core.database.CronosDatabase
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.ChronometerWithLapsDao
import com.nei.cronos.core.database.daos.LapDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun databaseProvider(
        @ApplicationContext context: Context
    ): CronosDatabase = CronosDatabase.getDatabase(context)

    @Provides
    fun chronometerDaoProvider(
        database: CronosDatabase,
    ): ChronometerDao = database.chronometerDao()

    @Provides
    fun lapDaoProvider(
        database: CronosDatabase,
    ): LapDao = database.lapDao()

    @Provides
    fun chronometerWithLapDaoProvider(
        database: CronosDatabase
    ): ChronometerWithLapsDao = database.chronometerWithLapsDao()
}