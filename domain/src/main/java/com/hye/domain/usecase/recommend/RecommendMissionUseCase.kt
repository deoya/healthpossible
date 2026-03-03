package com.hye.domain.usecase.recommend

import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.profile.UserProfile
import javax.inject.Inject

//추천 시스템
class RecommendMissionUseCase @Inject constructor(
    private val filterSafeMission: FilterSafeMissionUseCase,
    private val matchHabitMission: MatchHabitMissionUseCase,
    private val scaleMissionDifficulty: ScaleMissionDifficultyUseCase
) {
    // 💡 임시 더미 템플릿 풀 (이후 Repository에서 가져오도록 변경)
    private val templatePool = emptyList<Mission>()

    operator fun invoke(profile: UserProfile): List<Mission> {
        // 1단계: 안전 최우선 필터링
        val safeMissions = filterSafeMission(
            templates = templatePool,
            painPoints = profile.painPoints
        )

        // 2단계: 유저 목표 및 습관 매칭
        val matchedMissions = matchHabitMission(
            safeMissions = safeMissions,
            goals = profile.healthGoals,
            badHabits = profile.badHabits
        )

        // 3단계: 유저 체력 기반 난이도 조절
        val finalRecommendedMissions = scaleMissionDifficulty(
            missions = matchedMissions,
            activityLevel = profile.activityLevel
        )

        // 상위 3개만 잘라서 반환
        return finalRecommendedMissions.take(3)
    }
}