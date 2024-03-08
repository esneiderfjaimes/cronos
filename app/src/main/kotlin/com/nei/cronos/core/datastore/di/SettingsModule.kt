package com.nei.cronos.core.datastore.di

import android.content.Context
import com.nei.cronos.core.datastore.data.SettingsRepositoryImpl
import com.nei.cronos.core.datastore.domain.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Singleton
    @Provides
    fun provideSettingsRepository(
        @ApplicationContext context: Context,
    ): SettingsRepository = SettingsRepositoryImpl(context)

}