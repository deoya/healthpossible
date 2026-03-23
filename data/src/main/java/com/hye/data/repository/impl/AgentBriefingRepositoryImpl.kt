package com.hye.data.repository.impl

import com.google.firebase.vertexai.GenerativeModel
import com.hye.data.local.AiQuotaManager
import com.hye.domain.model.agent.AgentRecommendationData
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.repository.AgentBriefingRepository
import com.hye.domain.result.AgentRecommendationResult
import org.json.JSONObject
import javax.inject.Inject

class AgentBriefingRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val quotaManager: AiQuotaManager
) : AgentBriefingRepository {

    override suspend fun generateRecommendation(
        profile: UserProfile,
        safeMissions: List<Mission>,
        guidelineText: String?, // 1. RecommendMissionUseCase에서 넘겨줄 질병청 지침 추가
        userFeedback: String?
    ): AgentRecommendationResult<AgentRecommendationData> {

        if (safeMissions.isEmpty()) {
            return AgentRecommendationResult.Error(
                exception = IllegalStateException("No safe missions"),
                fallbackBriefing = "현재 하달할 수 있는 새로운 작전이 없습니다."
            )
        }

        if (!quotaManager.canCallAi()) {
            return AgentRecommendationResult.Error(
                exception = IllegalStateException("Quota Exceeded"),
                fallbackBriefing = "일일 전술 분석 통신망 한도를 초과했습니다. 로컬 데이터를 기반으로 수립된 예비 작전을 하달합니다."
            )
        }

        return try {
            // 2. 가이드라인 텍스트를 프롬프트 조립기에 전달
            val prompt = buildPrompt(profile, safeMissions, guidelineText, userFeedback)

            val response = generativeModel.generateContent(prompt)
            quotaManager.incrementCallCount()

            val responseText = response.text?.trim()
                ?: return AgentRecommendationResult.Error(IllegalStateException("Empty Response"), getDefaultMessage())

            // JSON 파싱 방어 로직
            val cleanJsonStr = responseText.replace("```json", "").replace("```", "").trim()
            val jsonObject = JSONObject(cleanJsonStr)

            val missionIdsArray = jsonObject.getJSONArray("missionIds")
            val selectedIds = List(missionIdsArray.length()) { i -> missionIdsArray.getString(i) }
            val briefing = jsonObject.getString("briefing")

            AgentRecommendationResult.Success(
                data = AgentRecommendationData(
                    selectedMissionIds = selectedIds,
                    briefing = briefing
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            AgentRecommendationResult.Error(e, getDefaultMessage())
        }
    }

    // 3. 프롬프트 엔지니어링 고도화
    private fun buildPrompt(
        profile: UserProfile,
        missions: List<Mission>,
        guidelineText: String?,
        userFeedback: String?
    ): String {
        val missionTitles = missions.joinToString(", ") { "- ID: ${it.id}, 제목: ${it.title}" }
        val painPointsStr = profile.painPoints.joinToString(", ").ifEmpty { "특이사항 없음" }
        val badHabitsStr = profile.badHabits?.joinToString(", ")?.ifEmpty { "특이사항 없음" } ?: "특이사항 없음"
        val chronicDiseasesStr = profile.chronicDiseases?.filter { it != "특별히 없음 (해당 사항 없음)" }?.joinToString(", ")?.ifEmpty { "없음" } ?: "없음"

        // 1. 질병청 가이드라인 섹션 복구
        val guidelineSection = if (!guidelineText.isNullOrBlank()) {
            """
            [국가 공식 건강 지침 (최우선 준수 사항!)]
            요원의 만성질환에 대한 국가 공식 가이드라인입니다. 반드시 이 지침을 최우선 근거로 삼아 작전을 선정하십시오.
            $guidelineText
            """.trimIndent()
        } else {
            "[국가 공식 건강 지침]\n특이 만성질환 없음. 일반적인 건강 증진 및 체력 훈련 목적으로 작전을 하달하십시오."
        }

        // 2. 사용자의 피드백 섹션
        val feedbackSection = if (!userFeedback.isNullOrBlank()) {
            """
            [요원 요청 사항 (실시간 피드백 반영)]
            요원이 이전 작전에 대해 다음과 같은 요청을 하였습니다: "$userFeedback"
            
            [조정 명령]
            1. 요원의 요청을 적극 반영하여 작전을 다시 선발하십시오. (예: 특정 작전을 빼달라고 하면 다른 작전으로 교체)
            2. 단, 요원의 '취약 지점', '나쁜 습관', 또는 '국가 공식 건강 지침'을 고려했을 때 절대적으로 필요한 작전을 빼달라고 요청했다면, 요청대로 빼주되 브리핑(briefing) 대사에 반드시 엄중히 경고하십시오.
            """.trimIndent()
        } else {
            ""
        }

        return """
            당신은 'Healthposable' 본부의 최고 엘리트 건강 관리 AI 에이전트입니다.
            요원님의 신체 스캔 결과와 국가 지침을 바탕으로, 아래 후보 작전 중 가장 적합한 5개를 골라 JSON 형식으로 보고하십시오.

            [요원 신체 스캔 결과]
            - 취약 지점: $painPointsStr
            - 중화 필요 타겟(나쁜 습관): $badHabitsStr
            - 보유 만성질환: $chronicDiseasesStr
            
            $guidelineSection
            
            $feedbackSection
            
            [후보 작전 목록]
            $missionTitles
            
            [응답 명령]
            1. 요원에게 하달할 5개의 작전 ID(missionIds)를 배열로 담으십시오.
            2. 요원에게 전하는 3문장 이내의 브리핑(briefing) 대사를 작성하십시오. 새로운 작전을 지어내지 마십시오.
            3. 절대 다른 말은 하지 말고 아래 JSON 구조로만 응답하십시오.
            {
              "missionIds": ["ID1", "ID2", "ID3", "ID4", "ID5"],
              "briefing": "브리핑 텍스트..."
            }
        """.trimIndent()
    }

    private fun getDefaultMessage() = "코드네임 식별 완료. 요원님의 맞춤형 작전입니다."
}