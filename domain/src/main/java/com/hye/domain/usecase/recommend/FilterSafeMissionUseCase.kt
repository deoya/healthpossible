package com.hye.domain.usecase.recommend

import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.Mission
import javax.inject.Inject

//통증 부위 필터링
class FilterSafeMissionUseCase @Inject constructor() {
    operator fun invoke(templates: List<Mission>, painPoints: List<String>): List<Mission> {
        // 점진적 온보딩: 통증 데이터가 아직 없으면 필터링 없이 전부 통과
        if (painPoints.isEmpty()) return templates

        val painPointsStr = painPoints.joinToString()

        return templates.filterNot { mission ->
            var isDangerous = false

            // 🔥 운동 미션일 경우에만 관절/통증 위험도 검사
            if (mission is ExerciseMission) {
                if ((painPointsStr.contains("무릎") || painPointsStr.contains("발목")) &&
                    (mission.selectedExercise == AiExerciseType.SQUAT ||
                            mission.selectedExercise == AiExerciseType.LUNGE ||
                            mission.selectedExercise == null) // null은 러닝(야외 기동)을 의미함
                ) {
                    isDangerous = true
                }

                // Todo : 검사항목 추가
            }

            // isDangerous가 true인 미션은 리스트에서 영구 제명(Drop)
            isDangerous
        }
    }
}