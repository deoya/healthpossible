package com.hye.data.repository.impl

import com.google.firebase.vertexai.GenerativeModel
import com.hye.data.local.AiQuotaManager
import com.hye.domain.model.agent.AgentRecommendationData
import com.hye.domain.model.agent.AgentResponseType
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
        activeMissions: List<Mission>,
        guidelineText: String?,
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
            // 1. 완벽하게 조립된 프롬프트 생성
            val prompt = buildPrompt(profile, safeMissions, activeMissions, guidelineText, userFeedback)
            // 2. AI 모델 호출
            val response = generativeModel.generateContent(prompt)
            quotaManager.incrementCallCount()

            val responseText = response.text?.trim()
                ?: return AgentRecommendationResult.Error(IllegalStateException("Empty Response"), getDefaultMessage())

            // 3. JSON 파싱 방어 로직 (마크다운 백틱 제거)
            val cleanJsonStr = responseText.replace("```json", "").replace("```", "").trim()
            val jsonObject = JSONObject(cleanJsonStr)

            val typeStr = jsonObject.optString("type", "CHAT")
            val responseType = try {
                AgentResponseType.valueOf(typeStr.uppercase())
            } catch (e: Exception) {
                AgentResponseType.CHAT
            }

            val missionIdsArray = jsonObject.optJSONArray("missionIds")
            val selectedIds = mutableListOf<String>()
            if (missionIdsArray != null) {
                for (i in 0 until missionIdsArray.length()) {
                    selectedIds.add(missionIdsArray.getString(i))
                }
            }

            val briefing = jsonObject.getString("briefing")

            AgentRecommendationResult.Success(
                data = AgentRecommendationData(
                    type = responseType,
                    selectedMissionIds = selectedIds,
                    briefing = briefing
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            AgentRecommendationResult.Error(e, getDefaultMessage())
        }
    }

    // 🔥 프롬프트 엔지니어링: 현재 수행 중인 작전(activeMissions) 반영
    private fun buildPrompt(
        profile: UserProfile,
        missions: List<Mission>,
        activeMissions: List<Mission>,
        guidelineText: String?,
        userFeedback: String?
    ): String {
        // 후보 미션 리스트
        val missionTitles = missions.joinToString(", ") { "- ID: ${it.id}, 제목: ${it.title}" }

        // 🔥 현재 요원이 수행 중인 미션 리스트 문자열화
        val activeMissionTitles = if (activeMissions.isNotEmpty()) {
            activeMissions.joinToString(", ") { "- ID: ${it.id}, 제목: ${it.title}" }
        } else {
            "현재 수행 중인 작전 없음"
        }

        // 신체 데이터 변환
        val painPointsStr = profile.painPoints.joinToString(", ").ifEmpty { "특이사항 없음" }
        val badHabitsStr = profile.badHabits?.joinToString(", ")?.ifEmpty { "특이사항 없음" } ?: "특이사항 없음"
        val chronicDiseasesStr = profile.chronicDiseases?.filter { it != "특별히 없음 (해당 사항 없음)" }?.joinToString(", ")?.ifEmpty { "없음" } ?: "없음"

        // 질병청 가이드라인 병합
        val guidelineSection = if (!guidelineText.isNullOrBlank()) {
            """
            [국가 공식 건강 지침 (최우선 준수 사항!)]
            요원의 만성질환에 대한 국가 공식 가이드라인입니다. 반드시 이 지침을 최우선 근거로 삼아 작전을 선정하거나 조언하십시오.
            $guidelineText
            """.trimIndent()
        } else {
            "[국가 공식 건강 지침]\n특이 만성질환 없음. 일반적인 건강 증진 및 체력 훈련 목적으로 작전을 하달하십시오."
        }

        // 요원 실시간 피드백 병합
        val feedbackSection = if (!userFeedback.isNullOrBlank()) {
            """
            [요원 실시간 요청 사항]
            "$userFeedback"
            """.trimIndent()
        } else {
            "[요원 실시간 요청 사항]\n없음 (정기 브리핑 요청)"
        }

        return """
            당신은 \'Healthposable\' 본부의 최고 엘리트 건강 관리 AI 에이전트(J.A.R.V.I.S 와 같은 존재)입니다.
            요원의 요청을 분석하여 높은 자유도의 대화를 수행하되, 반드시 아래의 지침과 JSON 형식에 맞춰 응답하십시오.

            [요원 신체 스캔 결과]
            - 취약 지점: $painPointsStr
            - 중화 필요 타겟(나쁜 습관): $badHabitsStr
            - 보유 만성질환: $chronicDiseasesStr
            
            $guidelineSection

            [현재 수행 중인 작전 목록]
            $activeMissionTitles

            [후보 작전 목록 (새로 추천하거나 교체할 때 참고할 목록)]
            $missionTitles

            $feedbackSection
            
            [작전 수행 지침 (의도 파악)]
            아래 4가지 상황 중 하나를 판단하여 \'type\'을 결정하십시오.
            1. CHAT: 단순 대화 (missionIds: 빈 배열)
            2. CONFIRM_DELETE: 요원이 [현재 수행 중인 작전 목록]에 있는 특정 작전을 빼달라고 할 때. 건강상 비추천 이유를 설명하고 재확인하십시오. (missionIds: 빈 배열)
            3. CONFIRM_CHANGE: 요원이 [현재 수행 중인 작전 목록]의 작전을 다른 것으로 바꿔달라고 할 때. [후보 작전 목록]에서 대안을 찾아 제안하십시오. (missionIds: 대안 작전 ID 1~2개)
            4. RECOMMEND: 정기 브리핑이거나 새로운 추천을 원할 때. (missionIds: 추천 작전 ID 5개)
            
            [응답 명령]
            절대 다른 말은 덧붙이지 말고 오직 아래 JSON 구조로만 응답하십시오.
            {
              "type": "CHAT 또는 CONFIRM_DELETE 또는 CONFIRM_CHANGE 또는 RECOMMEND",
              "briefing": "요원과 직접 대화하는 형태의 브리핑/답변/잔소리 (자연스러운 구어체)",
              "missionIds": ["ID1", "ID2"] 
            }
        """.trimIndent()
    }

    private fun getDefaultMessage() = "통신 상태가 불안정합니다. 요원님, 다시 한번 말씀해 주시겠습니까?"
}