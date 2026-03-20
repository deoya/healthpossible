package com.hye.domain.repository

import com.hye.domain.result.MissionResult

interface DiseaseGuidelineRepository {

    /**
     * 질병관리청 OpenAPI를 호출하여 만성질환에 대한 가이드라인 텍스트를 가져옵니다.
     * @param diseaseName 만성질환명 (예: "당뇨병")
     * @return HTML이 제거되고 핵심 내용만 정제된 순수 텍스트 결과
     */
    suspend fun fetchDiseaseGuideline(diseaseName: String): MissionResult<String>

    /**
     * AI 프롬프트 주입 및 빠른 로딩을 위해 로컬(Room/DataStore)에 캐싱된 지침을 가져옵니다.
     */
    suspend fun getCachedGuideline(diseaseName: String): MissionResult<String?>

    /**
     * 서버에서 가져온 정제된 텍스트 지침을 로컬에 임시 저장합니다.
     */
    suspend fun saveGuidelineToCache(diseaseName: String, guideline: String): MissionResult<Unit>
}