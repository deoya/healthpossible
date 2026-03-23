package com.hye.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hye.data.local.dao.DiseaseGuidelineDao
import com.hye.data.local.entity.DiseaseGuidelineEntity

@Database(
    entities = [DiseaseGuidelineEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun diseaseGuidelineDao(): DiseaseGuidelineDao

}