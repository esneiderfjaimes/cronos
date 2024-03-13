package cronos.core.data.di

import cronos.core.data.repository.LocalRepository
import cronos.core.data.repository.LocalRepositoryImpl
import cronos.core.data.repository.LocalSettingsRepository
import cronos.core.data.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun localRepositoryBind(
        localRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository

    @Binds
    fun settingsRepositoryBind(
        settingsRepository: LocalSettingsRepository
    ): SettingsRepository
}