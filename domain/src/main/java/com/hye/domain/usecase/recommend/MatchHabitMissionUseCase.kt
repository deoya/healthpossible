package com.hye.domain.usecase.recommend

import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.RoutineMission
import javax.inject.Inject

//목표/습관 매칭 UseCase
class MatchHabitMissionUseCase @Inject constructor() {
    operator fun invoke(safeMissions: List<Mission>, goals: List<String>, badHabits: List<String>?): List<Mission> {
        return safeMissions.sortedByDescending { mission ->
            var score = 0
            if (goals.contains("자세 교정") && mission.title.contains("코어")) score += 10
            if (goals.contains("다이어트") && mission.title.contains("러닝")) score += 10
            if (badHabits?.contains("수분 부족") == true && mission is RoutineMission) score += 15
            score
        }
    }
}