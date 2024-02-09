package com.nei.cronos.core.data.di

import com.nei.cronos.core.data.LocalRepository
import com.nei.cronos.core.data.LocalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun localRepositoryBind(
        repositoryImpl: LocalRepositoryImpl
    ): LocalRepository
}