package com.hye.domain.usecase.recommend

import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.domain.model.profile.ActivityLevel
import javax.inject.Inject

//동적 난이도 조절
class ScaleMissionDifficultyUseCase @Inject constructor() {
    operator fun invoke(missions: List<Mission>, activityLevel: ActivityLevel?): List<Mission> {
        return missions.map { mission ->
            when (activityLevel) {
                ActivityLevel.BEGINNER -> {
                    // 초보자: 주 2회, 목표치 50%
                    mission.scaleDown(weeklyCount = 2, targetRatio = 0.5f)
                }
                ActivityLevel.EXPERT -> {
                    // 고수: 주 5회, 목표치 150%
                    mission.scaleDown(weeklyCount = 5, targetRatio = 1.5f)
                }
                ActivityLevel.NORMAL, null -> {
                    // 보통 혹은 정보가 없을 때(null): 기본값인 주 3회, 목표치 100%
                    mission.scaleDown(weeklyCount = 3, targetRatio = 1.0f)
                }
            }
        }
    }

    private fun Mission.scaleDown(weeklyCount: Int, targetRatio: Float): Mission {
        return when (this) {
            is ExerciseMission -> this.copy(
                weeklyTargetCount = weeklyCount,
                // 비율을 곱한 뒤 소수점을 버리고, 최소 1회(또는 1분)는 보장하도록 coerceAtLeast(1) 사용
                targetValue = (this.targetValue * targetRatio).toInt().coerceAtLeast(1)
            )
            is RoutineMission -> this.copy(weeklyTargetCount = weeklyCount)
            else -> this.updateCommon(weeklyTargetCount = weeklyCount)
        }
    }
}