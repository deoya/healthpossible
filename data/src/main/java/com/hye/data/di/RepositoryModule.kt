package com.hye.data.di

import com.hye.data.repository.impl.AuthRepositoryImpl
import com.hye.data.repository.impl.MissionRepositoryImpl
import com.hye.data.repository.impl.ProfileRepositoryImpl
import com.hye.domain.repository.AuthRepository
import com.hye.domain.repository.MissionRepository
import com.hye.domain.repository.ProfileRepository
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

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}