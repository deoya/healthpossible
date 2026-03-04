package com.hye.domain.usecase.recommend

import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.domain.model.profile.ActivityLevel
import javax.inject.Inject

//동적 난이도 조절
class ScaleMissionDifficultyUseCase @Inject constructor() {
    operator fun invoke(mission: Mission, activityLevel: ActivityLevel?): Mission {
        if (activityLevel == null || activityLevel == ActivityLevel.NORMAL) {
            return mission
        }

        return when (mission) {
            // 🔥 1. 운동 미션: 타겟 횟수(targetValue) 조절
            is ExerciseMission -> {
                val baseTarget = mission.selectedExercise?.defaultTarget ?: mission.targetValue

                // 체력 수준에 따른 배율(Multiplier) 설정
                val multiplier = when (activityLevel) {
                    ActivityLevel.BEGINNER -> 0.6
                    ActivityLevel.EXPERT -> 1.4
                    null, ActivityLevel.NORMAL -> 1.0 // 🔥 점진적 온보딩 (null이거나 보통이면 1.0배)
                }

                val scaledTarget = (baseTarget * multiplier).toInt().coerceAtLeast(1)

                mission.copy(targetValue = scaledTarget)
            }

            // 🔥 2. 상시 미션(루틴): 걷기 걸음 수 조절
            is RoutineMission -> {
                if (mission.title.contains("걷기") || mission.id == "T_RT_03") {
                    val baseTarget = mission.dailyTargetAmount

                    val multiplier = when (activityLevel) {
                        ActivityLevel.BEGINNER -> 0.7
                        ActivityLevel.EXPERT -> 1.3
                        null, ActivityLevel.NORMAL -> 1.0 // 🔥 점진적 온보딩
                    }

                    val scaledSteps = (baseTarget * multiplier).toInt()
                    mission.copy(dailyTargetAmount = scaledSteps)
                } else {
                    mission // 물 마시기 등은 배율 없이 그대로 패스
                }
            }

            // 식단이나 제한 미션은 원본 그대로 반환
            else -> mission
        }
    }
}