package com.hye.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hye.data.local.entity.DiseaseGuidelineEntity

@Dao
interface DiseaseGuidelineDao {

    // 질환명으로 캐싱된 지침을 가져옵니다. (없으면 null 반환)
    @Query("SELECT * FROM disease_guideline_table WHERE diseaseName = :diseaseName LIMIT 1")
    suspend fun getGuideline(diseaseName: String): DiseaseGuidelineEntity?

    // 새로운 지침을 저장합니다. (이미 같은 질환명의 데이터가 있다면 최신 내용으로 덮어씁니다)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuideline(guideline: DiseaseGuidelineEntity)

    // 캐시를 초기화해야 할 때
    @Query("DELETE FROM disease_guideline_table")
    suspend fun clearAllGuidelines()
}