package com.hye.data.api.service

import com.hye.data.BuildConfig
import com.hye.data.api.mapper.DiseaseGuidelineResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DiseaseApiService {
    @GET("api/provide/healthInfoNew")
    suspend fun fetchDiseaseGuideline(
        @Query("TOKEN") token: String = BuildConfig.KDCA_API_TOKEN, // 🔥 자동 주입
        @Query("cntntsSn") contentSn: String
    ): DiseaseGuidelineResponse
}