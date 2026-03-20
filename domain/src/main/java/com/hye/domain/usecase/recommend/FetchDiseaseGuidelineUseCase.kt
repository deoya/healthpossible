package com.hye.domain.usecase.recommend

import com.hye.domain.repository.DiseaseGuidelineRepository
import com.hye.domain.result.MissionResult
import javax.inject.Inject

class FetchDiseaseGuidelineUseCase @Inject constructor(
    private val repository: DiseaseGuidelineRepository
) {
    suspend operator fun invoke(diseaseName: String): MissionResult<String> {
        // 1. 캐시 확인 로직 (현재는 미구현이므로 통과)
        // val cached = repository.getCachedGuideline(diseaseName)
        // if (cached is MissionResult.Success && cached.resultData != null) return cached

        // 2. 캐시가 없으면 실제 서버 API 호출
        return repository.fetchDiseaseGuideline(diseaseName)
    }
}