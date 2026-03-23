package com.hye.data.di

import android.content.Context
import androidx.room.Room
import com.hye.data.local.AppDatabase
import com.hye.data.local.dao.DiseaseGuidelineDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "healthposable_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDiseaseGuidelineDao(database: AppDatabase): DiseaseGuidelineDao {
        return database.diseaseGuidelineDao()
    }
}