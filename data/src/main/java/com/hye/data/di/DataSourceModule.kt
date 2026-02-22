package com.hye.data.di

import com.hye.data.datasource.auth.AuthDataSource
import com.hye.data.datasource.auth.FirebaseAuthDataSource
import com.hye.data.datasource.mission.FirebaseMissionDataSource
import com.hye.data.datasource.mission.MissionDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMissionDataSource(impl: FirebaseMissionDataSource): MissionDataSource

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(impl: FirebaseAuthDataSource): AuthDataSource
}
