package com.hye.data.di

import com.hye.data.datasource.FirebaseMissionDataSource
import com.hye.data.datasource.MissionDataSource
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
    abstract fun bindMissionDataSource(firebaseMissionDataSource: FirebaseMissionDataSource): MissionDataSource
}
