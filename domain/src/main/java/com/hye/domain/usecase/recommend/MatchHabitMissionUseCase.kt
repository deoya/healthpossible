package com.hye.domain.usecase.recommend

import com.hye.domain.model.mission.RecommendedMission
import com.hye.domain.model.mission.Suitability
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.domain.model.profile.UserProfile
import javax.inject.Inject

//목표/습관 매칭 UseCase
class MatchHabitMissionUseCase @Inject constructor() {
    operator fun invoke(safeMissions: List<Mission>, profile: UserProfile): List<RecommendedMission> {
        return safeMissions.map { mission ->
            val score = calculateScore(mission, profile)

            val suitability = when {
                score >= 3 -> Suitability.HIGH
                score >= 1 -> Suitability.MEDIUM
                else -> Suitability.LOW
            }

            RecommendedMission(mission = mission, suitability = suitability)
        }
    }

    private fun calculateScore(mission: Mission, profile: UserProfile): Int {
        var score = 0
        val painPointsStr = profile.painPoints.joinToString()
        val badHabitsStr = profile.badHabits?.joinToString() ?: ""
        val goalsStr = profile.healthGoals.joinToString()

        if (mission is ExerciseMission) {
            if (painPointsStr.contains("허리") && mission.selectedExercise == AiExerciseType.PLANK) score += 3
            if ((painPointsStr.contains("하체") || painPointsStr.contains("무릎")) &&
                (mission.selectedExercise == AiExerciseType.SQUAT || mission.selectedExercise == AiExerciseType.LUNGE)) score += 3
        }

        if (mission is RestrictionMission) {
            if (badHabitsStr.contains("야식") && mission.title.contains("야간 취식")) score += 3
            if (badHabitsStr.contains("스마트폰") && mission.title.contains("스마트폰")) score += 3
            if (badHabitsStr.contains("카페인") && mission.title.contains("카페인")) score += 3
        }

        if (mission is RoutineMission && goalsStr.contains("체력")) score += 1
        if (mission is DietMission && goalsStr.contains("다이어트")) score += 1
        if (mission.id == "T_RT_01") score += 1

        return score
    }
}