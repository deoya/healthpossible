package com.hye.data.api.impl

import com.hye.data.api.service.DiseaseApiService
import com.hye.data.common.cleanHtmlAndCdata
import com.hye.domain.model.profile.ChronicDiseaseCode
import com.hye.domain.repository.DiseaseGuidelineRepository
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiseaseGuidelineRepositoryImpl @Inject constructor(
    private val apiService: DiseaseApiService
    // Todo : 추후 로컬 캐싱을 위해 Room Dao나 DataStore가 주입될 자리
) : DiseaseGuidelineRepository {

    override suspend fun fetchDiseaseGuideline(diseaseName: String): MissionResult<String> {
        return withContext(Dispatchers.IO) {
            try {
                val targetSn = ChronicDiseaseCode.getSnByName(diseaseName)
                    ?: return@withContext MissionResult.Error(Exception("지원하지 않는 질환입니다: $diseaseName"))

                val response = apiService.fetchDiseaseGuideline(
                    contentSn = targetSn
                )
                // 1. API 자체의 에러 코드 방어
                if (response.head?.code != "S001" && response.head?.message != "OK") {
                    return@withContext MissionResult.Error(Exception("API 통신 오류: ${response.head?.message}"))
                }

                // 2. 본문 데이터 추출
                val contentItems = response.svc?.contentList?.contents
                if (contentItems.isNullOrEmpty()) {
                    return@withContext MissionResult.Error(Exception("해당 질환($diseaseName)에 대한 국가 건강 지침이 존재하지 않습니다."))
                }

                // 3. HTML 태그 정제 및 텍스트 압축
                val extractedText = StringBuilder()
                extractedText.append("[국가 공식 건강 지침: ${response.svc.title}]\n")

                contentItems.forEach { item ->
                    val category = item.categoryName ?: "기타 분류"
                    val htmlRaw = item.htmlContent ?: ""

                    val cleanText = cleanHtmlAndCdata(htmlRaw)
                    if (cleanText.isNotEmpty()) {
                        extractedText.append("\n[$category]\n$cleanText\n")
                    }
                }

                MissionResult.Success(extractedText.toString())

            } catch (e: Exception) {
                e.printStackTrace()
                MissionResult.Error(e)
            }
        }
    }

    override suspend fun getCachedGuideline(diseaseName: String): MissionResult<String?> {
        // Todo: 향후 작전에서 Room DB 캐시 읽기 구현
        return MissionResult.Success(null)
    }

    override suspend fun saveGuidelineToCache(diseaseName: String, guideline: String): MissionResult<Unit> {
        // Todo: 향후 작전에서 Room DB 저장 구현
        return MissionResult.Success(Unit)
    }
}