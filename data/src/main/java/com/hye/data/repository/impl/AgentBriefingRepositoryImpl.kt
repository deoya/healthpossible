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
        guidelineText: String? // 1. RecommendMissionUseCase에서 넘겨줄 질병청 지침 추가
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
            val prompt = buildPrompt(profile, safeMissions, guidelineText)

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
    private fun buildPrompt(profile: UserProfile, missions: List<Mission>, guidelineText: String?): String {
        val missionListStr = missions.joinToString("\n") { "- ID: ${it.id}, 제목: ${it.title}, 목적: ${it.memo}" }
        val painPointsStr = profile.painPoints.joinToString(", ").ifEmpty { "특이사항 없음" }
        val badHabitsStr = profile.badHabits?.joinToString(", ")?.ifEmpty { "특이사항 없음" } ?: "특이사항 없음"

        val chronicDiseasesStr = profile.chronicDiseases?.filter { it != "특별히 없음 (해당 사항 없음)" }?.joinToString(", ")?.ifEmpty { "없음" } ?: "없음"

        // 🔥 질병청 지침이 있을 때와 없을 때 프롬프트 섹션을 다르게 구성
        val guidelineSection = if (!guidelineText.isNullOrBlank()) {
            """
            [국가 공식 건강 지침 (최우선 준수 사항!)]
            요원의 만성질환에 대한 국가 공식 가이드라인입니다. 반드시 이 지침을 최우선 근거로 삼아 작전을 선정하고 브리핑에 반영하십시오.
            $guidelineText
            """.trimIndent()
        } else {
            "[국가 공식 건강 지침]\n특이 만성질환 없음. 일반적인 건강 증진 및 체력 훈련 목적으로 작전을 하달하십시오."
        }

        return """
            당신은 'Healthposable' 본부의 최고 엘리트 건강 관리 AI 에이전트입니다.
            요원의 신체 상태와 국가 공식 건강 지침을 엄격하게 분석하여, 제공된 후보 작전 중 가장 안전하고 효과적인 작전 5개를 선정하고 브리핑을 작성하십시오.

            === [요원 데이터베이스] ===
            - 취약 지점(통증): $painPointsStr
            - 중화 필요 타겟(나쁜 습관): $badHabitsStr
            - 보유 만성질환: $chronicDiseasesStr
            - 활동 수준: ${profile.activityLevel?.label ?: "보통"}

            === $guidelineSection ===
            
            === [후보 작전 목록] ===
            $missionListStr
            
            === [명령 하달 지침] ===
            1. (선정) 위 후보 작전 중 요원에게 가장 적합한 5개의 작전 ID(missionIds)를 골라 배열로 담으십시오.
            2. (브리핑) 요원에게 하달하는 3문장 이내의 브리핑(briefing) 대사를 작성하십시오. 단호하고 전문적인 군사/에이전트 톤을 유지하십시오.
            3. (근거) 만성질환이 있다면, 브리핑 내용에 "국가 가이드라인에 의거하여~" 등의 방식으로 지침을 참고했음을 자연스럽게 녹여내십시오.
            4. (형식) 절대 다른 말은 하지 말고, 오직 아래 JSON 구조로만 응답하십시오.

            {
              "missionIds": ["ID1", "ID2", "ID3", "ID4", "ID5"],
              "briefing": "브리핑 텍스트..."
            }
        """.trimIndent()
    }

    private fun getDefaultMessage() = "코드네임 식별 완료. 요원님의 맞춤형 작전입니다."
}