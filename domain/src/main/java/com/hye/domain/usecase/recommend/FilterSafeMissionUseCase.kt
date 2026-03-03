package com.hye.domain.usecase.recommend

import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.Mission
import javax.inject.Inject

//통증 부위 필터링
class FilterSafeMissionUseCase @Inject constructor() {
    operator fun invoke(templates: List<Mission>, painPoints: List<String>): List<Mission> {
        return templates.filterNot { mission ->
            when {
                // 무릎/관절 통증이 있는데, 하체 중심인 경우 제외
                painPoints.any { it.contains("무릎") || it.contains("관절") } &&
                        mission is ExerciseMission &&
                        (mission.selectedExercise == AiExerciseType.SQUAT || mission.selectedExercise == AiExerciseType.LUNGE) -> true

                else -> false
            }
        }
    }
}