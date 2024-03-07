package com.nei.cronos.core.database.di

import android.content.Context
import com.nei.cronos.core.database.CronosDatabase
import com.nei.cronos.core.database.daos.ChronometerDao
import com.nei.cronos.core.database.daos.EventDao
import com.nei.cronos.core.database.daos.SectionDao
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
    fun sectionDaoProvider(
        database: CronosDatabase
    ): SectionDao = database.sectionDao()

    @Provides
    fun chronometerDaoProvider(
        database: CronosDatabase,
    ): ChronometerDao = database.chronometerDao()

    @Provides
    fun lapDaoProvider(
        database: CronosDatabase,
    ): EventDao = database.lapDao()
}