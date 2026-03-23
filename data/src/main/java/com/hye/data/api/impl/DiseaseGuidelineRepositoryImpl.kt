package com.hye.data.api.impl

import com.hye.data.api.service.DiseaseApiService
import com.hye.data.common.cleanHtmlAndCdata
import com.hye.data.local.dao.DiseaseGuidelineDao
import com.hye.data.local.entity.DiseaseGuidelineEntity
import com.hye.domain.model.profile.ChronicDiseaseCode
import com.hye.domain.repository.DiseaseGuidelineRepository
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DiseaseGuidelineRepositoryImpl @Inject constructor(
    private val apiService: DiseaseApiService,
    private val guidelineDao: DiseaseGuidelineDao
) : DiseaseGuidelineRepository {

    override suspend fun fetchDiseaseGuideline(diseaseName: String): MissionResult<String> {
        return withContext(Dispatchers.IO) {
            try {
                // 🔥 1. 금고(Local DB) 선제 확인
                val cachedResult = getCachedGuideline(diseaseName)
                if (cachedResult is MissionResult.Success) {
                    cachedResult.resultData?.let { data ->
                        return@withContext MissionResult.Success(data)
                    }
                }

                // 🔥 2. 금고가 비어있다면 기존대로 질병청(Remote API) 통신망 연결
                val targetSn = ChronicDiseaseCode.getSnByName(diseaseName)
                    ?: return@withContext MissionResult.Error(Exception("지원하지 않는 질환입니다: $diseaseName"))

                val response = apiService.fetchDiseaseGuideline(
                    contentSn = targetSn
                )

                // API 자체의 에러 코드 방어
                if (response.head?.code != "S001" && response.head?.message != "OK") {
                    return@withContext MissionResult.Error(Exception("API 통신 오류: ${response.head?.message}"))
                }

                // 본문 데이터 추출
                val contentItems = response.svc?.contentList?.contents
                if (contentItems.isNullOrEmpty()) {
                    return@withContext MissionResult.Error(Exception("해당 질환($diseaseName)에 대한 국가 건강 지침이 존재하지 않습니다."))
                }

                // HTML 태그 정제 및 텍스트 압축
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

                val finalGuidelineText = extractedText.toString()

                // 🔥 3. 새로 확보하고 정제한 데이터를 금고에 안전하게 보관
                saveGuidelineToCache(diseaseName, finalGuidelineText)

                // 최종 결과 반환
                MissionResult.Success(finalGuidelineText)

            } catch (e: Exception) {
                e.printStackTrace()
                MissionResult.Error(e)
            }
        }
    }

    override suspend fun getCachedGuideline(diseaseName: String): MissionResult<String?> {
        return try {
            val cachedEntity = guidelineDao.getGuideline(diseaseName)
            MissionResult.Success(cachedEntity?.guidelineContent)
        } catch (e: Exception) {
            MissionResult.Error(e)
        }
    }

    override suspend fun saveGuidelineToCache(diseaseName: String, guideline: String): MissionResult<Unit> {
        return try {
            val newEntity = DiseaseGuidelineEntity(
                diseaseName = diseaseName,
                guidelineContent = guideline,
                updatedAt = System.currentTimeMillis()
            )
            guidelineDao.insertGuideline(newEntity)
            MissionResult.Success(Unit)
        } catch (e: Exception) {
            MissionResult.Error(e)
        }
    }
}