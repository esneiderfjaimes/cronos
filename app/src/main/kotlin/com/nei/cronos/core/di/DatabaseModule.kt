package com.nei.cronos.core.di

import android.content.Context
import com.nei.cronos.core.database.ChronometerDao
import com.nei.cronos.core.database.CronosDatabase
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
}