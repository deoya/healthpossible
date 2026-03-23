package com.hye.domain.usecase.recommend

import com.hye.domain.model.mission.RecommendedMission
import com.hye.domain.model.mission.Suitability
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RestrictionType
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.repository.AgentBriefingRepository
import com.hye.domain.repository.DiseaseGuidelineRepository
import com.hye.domain.result.AgentRecommendationResult
import com.hye.domain.result.MissionResult
import com.hye.domain.result.RecommendationPipelineResult
import javax.inject.Inject

//추천 시스템
class RecommendMissionUseCase @Inject constructor(
    private val filterSafeMission: FilterSafeMissionUseCase,
    private val matchHabitMission: MatchHabitMissionUseCase,
    private val scaleMissionDifficulty: ScaleMissionDifficultyUseCase,
    private val agentRepository: AgentBriefingRepository,
    private val diseaseGuidelineRepository: DiseaseGuidelineRepository
) {

    suspend operator fun invoke(profile: UserProfile, userFeedback: String? = null): RecommendationPipelineResult {

        // 1단계: 안전 최우선 필터링
        val safeTemplates = filterSafeMission(templates = templatePool, painPoints = profile.painPoints)

        // 1.5단계: 만성질환 질병청 지침 확보
        var guidelineText: String? = null
        val chronicDiseases = profile.chronicDiseases

        if (!chronicDiseases.isNullOrEmpty() && chronicDiseases.first() != "특별히 없음 (해당 사항 없음)") {
            val targetDisease = chronicDiseases.first()
            val guidelineResult = diseaseGuidelineRepository.fetchDiseaseGuideline(targetDisease)

            if (guidelineResult is MissionResult.Success) {
                guidelineText = guidelineResult.resultData
            }
        }

        // 🔥 2단계: AI 통신 시도
        val aiResult = agentRepository.generateRecommendation(
            profile = profile,
            missions = safeTemplates,
            guidelineText = guidelineText, // 질병청 데이터!
            userFeedback = userFeedback    // 요원 피드백 데이터!
        )
        // 3단계: ROP 결과에 따른 분기 (when)
        val (finalMissions, finalBriefing) = when (aiResult) {
            is AgentRecommendationResult.Success -> {
                val aiData = aiResult.data
                // AI가 넘겨준 ID와 일치하는 미션 추출
                val selectedMissions = safeTemplates.filter { it.id in aiData.selectedMissionIds }.take(5)

                if (selectedMissions.isNotEmpty()) {
                    // AI가 선발한 미션은 적합도를 무조건 HIGH로 부여
                    val recommended = selectedMissions.map { RecommendedMission(it, Suitability.HIGH) }
                    Pair(recommended, aiData.briefing)
                } else {
                    // ID가 매칭되지 않는 오류 발생 시, 로컬 코드 가동
                    runLocalFallback(safeTemplates, profile, "AI 작전 코드 매칭 실패. 예비 통신망으로 전환합니다.")
                }
            }
            is AgentRecommendationResult.Error -> {
                // 한도 초과 등의 통신 에러 발생 시, 로컬 코드 가동
                runLocalFallback(safeTemplates, profile, aiResult.fallbackBriefing)
            }
        }



        // 4단계: 동적 난이도 조절
        val scaledRecommendations = finalMissions.map { recommendedItem ->
            val adjustedMission = scaleMissionDifficulty(
                mission = recommendedItem.mission,
                activityLevel = profile.activityLevel,
            )
            recommendedItem.copy(mission = adjustedMission)
        }

        return RecommendationPipelineResult(scaledRecommendations, finalBriefing)
    }

    private fun runLocalFallback(
        safeTemplates: List<Mission>,
        profile: UserProfile,
        fallbackMessage: String
    ): Pair<List<RecommendedMission>, String> {
        // 기존 2단계: 안전한 템플릿을 대상으로 적합도 채점
        val scoredMissions = matchHabitMission(safeMissions = safeTemplates, profile = profile)

        // 기존 3단계: 티어별 섞기 및 그룹핑
        val highMissions = scoredMissions.filter { it.suitability == Suitability.HIGH }.shuffled()
        val mediumMissions = scoredMissions.filter { it.suitability == Suitability.MEDIUM }.shuffled()
        val lowMissions = scoredMissions.filter { it.suitability == Suitability.LOW }.shuffled()

        // 기존 4단계: '상 -> 중 -> 하' 순서로 5개 추출
        val finalRecommendations = mutableListOf<RecommendedMission>()
        finalRecommendations.addAll(highMissions)

        if (finalRecommendations.size < 5) {
            finalRecommendations.addAll(mediumMissions.take(5 - finalRecommendations.size))
        }
        if (finalRecommendations.size < 5) {
            finalRecommendations.addAll(lowMissions.take(5 - finalRecommendations.size))
        }

        // 5개 추출 완료된 리스트와 대체 브리핑 메시지를 Pair로 묶어 반환
        return Pair(finalRecommendations.take(5), fallbackMessage)
    }
    // 🔥 테스트를 위한 더미 템플릿
    private val templatePool: List<Mission> = listOf(
        // --- [1] AI 코칭 & 러닝 운동 (ExerciseMission) ---
        ExerciseMission(
            id = "T_EX_01", title = "AI 코칭: 하체 타격 (스쿼트)", memo = "무너진 하체 밸런스를 복구하는 핵심 작전입니다.",
            notificationTime = null, weeklyTargetCount = 3, weekIdentifier = null, isTemplate = true,
            targetValue = 15, unit = ExerciseRecordMode.SELECTED, useSupportAgent = true, selectedExercise = AiExerciseType.SQUAT
        ),
        ExerciseMission(
            id = "T_EX_02", title = "AI 코칭: 코어 방어 (플랭크)", memo = "요통 예방 및 코어 장갑을 강화하는 작전입니다.",
            notificationTime = null, weeklyTargetCount = 4, weekIdentifier = null, isTemplate = true,
            targetValue = 60, unit = ExerciseRecordMode.SELECTED, useSupportAgent = true, selectedExercise = AiExerciseType.PLANK
        ),
        ExerciseMission(
            id = "T_EX_03", title = "AI 코칭: 하체 밸런스 (런지)", memo = "비대칭 밸런스 교정을 위한 전술적 훈련입니다.",
            notificationTime = null, weeklyTargetCount = 3, weekIdentifier = null, isTemplate = true,
            targetValue = 10, unit = ExerciseRecordMode.SELECTED, useSupportAgent = true, selectedExercise = AiExerciseType.LUNGE
        ),
        ExerciseMission(
            id = "T_EX_04", title = "전술적 야외 기동 (러닝)", memo = "심폐 지구력 확보를 위한 야외 기동 훈련입니다.",
            notificationTime = null, weeklyTargetCount = 2, weekIdentifier = null, isTemplate = true,
            targetValue = 30, unit = ExerciseRecordMode.RUNNING, useSupportAgent = true, selectedExercise = null
        ),

        // --- [2] 상시 습관 (RoutineMission) ---
        RoutineMission(
            id = "T_RT_01", title = "수분 보충 작전", memo = "신진대사 활성화 및 피로 회복을 위한 필수 수분 보급입니다.",
            notificationTime = null, weeklyTargetCount = 7, weekIdentifier = null, isTemplate = true,
            dailyTargetAmount = 2000, amountPerStep = 250, unitLabel = "ml"
        ),
        RoutineMission(
            id = "T_RT_02", title = "거북목 중화 스트레칭", memo = "장시간 모니터 주시로 인한 경추 타격을 회복합니다.",
            notificationTime = null, weeklyTargetCount = 5, weekIdentifier = null, isTemplate = true,
            dailyTargetAmount = 3, amountPerStep = 1, unitLabel = "회"
        ),
        RoutineMission(
            id = "T_RT_03", title = "일상 걷기 훈련", memo = "기초 체력 유지를 위한 최소한의 기동입니다.",
            notificationTime = null, weeklyTargetCount = 5, weekIdentifier = null, isTemplate = true,
            dailyTargetAmount = 8000, amountPerStep = 1000, unitLabel = "보"
        ),

        // --- [3] 제한 습관 (RestrictionMission) ---
        RestrictionMission(
            id = "T_RS_01", title = "야간 취식 통제", memo = "수면의 질 저하 및 위장 기능 교란을 막는 방어 작전입니다.",
            notificationTime = null, weeklyTargetCount = 5, weekIdentifier = null, isTemplate = true,
            type = RestrictionType.CHECK, maxAllowedMinutes = null
        ),
        RestrictionMission(
            id = "T_RS_02", title = "취침 전 스마트폰 차단", memo = "시신경 피로 누적 및 멜라토닌 분비 억제를 방지합니다.",
            notificationTime = null, weeklyTargetCount = 5, weekIdentifier = null, isTemplate = true,
            type = RestrictionType.TIMER, maxAllowedMinutes = 30
        ),
        RestrictionMission(
            id = "T_RS_03", title = "카페인 과다 섭취 제한", memo = "중추신경계 과각성을 막고 안정적인 수면을 확보합니다.",
            notificationTime = null, weeklyTargetCount = 6, weekIdentifier = null, isTemplate = true,
            type = RestrictionType.CHECK, maxAllowedMinutes = null
        ),

        // --- [4] 식단 습관 (DietMission) ---
        DietMission(
            id = "T_DT_01", title = "조식 보급 인증", memo = "오전 작전 수행을 위한 초기 에너지 밸런스를 확보합니다.",
            notificationTime = null, weeklyTargetCount = 5, weekIdentifier = null, isTemplate = true,
            recordMethod = DietRecordMethod.PHOTO
        ),
        DietMission(
            id = "T_DT_02", title = "클린 식단 (채소) 보고", memo = "식이섬유 보급을 통해 체내 불순물을 제거하는 작전입니다.",
            notificationTime = null, weeklyTargetCount = 4, weekIdentifier = null, isTemplate = true,
            recordMethod = DietRecordMethod.PHOTO
        )
    )
}


