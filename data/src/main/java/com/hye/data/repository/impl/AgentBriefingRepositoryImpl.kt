package com.hye.data.repository.impl

import com.google.firebase.vertexai.GenerativeModel
import com.hye.data.local.AiQuotaManager
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.repository.AgentBriefingRepository
import javax.inject.Inject

class AgentBriefingRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val quotaManager: AiQuotaManager
) : AgentBriefingRepository {

    override suspend fun generateBriefing(profile: UserProfile, missions: List<Mission>): String {
        if (missions.isEmpty()) return "현재 하달할 수 있는 새로운 작전이 없습니다."

        if (!quotaManager.canCallAi()) {
            return "일일 전술 분석 통신망 한도를 초과했습니다. 요원님의 데이터를 기반으로 수립된 기본 작전을 즉시 하달합니다."
        }
        val prompt = buildPrompt(profile, missions)

        return try {
            val response = generativeModel.generateContent(prompt)
            quotaManager.incrementCallCount()

            response.text?.trim() ?: getDefaultMessage()
        } catch (e: Exception) {
            e.printStackTrace()
            getDefaultMessage()
        }
    }

    private fun buildPrompt(profile: UserProfile, missions: List<Mission>): String {
        val missionTitles = missions.joinToString(", ") { it.title }
        val painPointsStr = profile.painPoints.joinToString(", ").ifEmpty { "특이사항 없음" }
        val badHabitsStr = profile.badHabits?.joinToString(", ")?.ifEmpty { "특이사항 없음" } ?: "특이사항 없음"

        return """
            [요원 신체 스캔 결과]
            - 취약 지점: $painPointsStr
            - 중화 필요 타겟(나쁜 습관): $badHabitsStr
            
            [오늘 하달할 작전 ${missions.size}가지]
            $missionTitles
            
            [명령]
            요원님의 신체 스캔 결과를 바탕으로, 위 ${missions.size}가지 작전을 이번 주에 추천하는 이유를 브리핑해.
            반드시 3문장 이내로 짧고 강렬하게 보고하고, 새로운 작전을 지어내지 마.
        """.trimIndent()
    }

    private fun getDefaultMessage() = "코드네임 식별 완료. 요원님의 맞춤형 작전입니다."
}