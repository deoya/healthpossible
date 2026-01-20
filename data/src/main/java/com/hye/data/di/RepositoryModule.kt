package com.hye.data.di

import com.hye.data.repository.impl.MissionRepositoryImpl
import com.hye.domain.repository.MissionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMissionRepository(impl: MissionRepositoryImpl): MissionRepository
}