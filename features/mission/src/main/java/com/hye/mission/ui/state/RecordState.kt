package com.hye.mission.ui.state

import com.hye.domain.model.mission.types.AiSessionMode
import com.hye.domain.model.mission.types.ExerciseAgentType


// UI 상태 정의
data class RecordState(
    // 공통 정보
    val exerciseAgentType: ExerciseAgentType = ExerciseAgentType.AI_POSTURE,
    val feedbackMessage: String = "준비해주세요.",
    val isBottomSheetOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // 세션 모드 (초기값은 임시로 DurationMode)
    val sessionMode: AiSessionMode = AiSessionMode.DurationMode(),

    val isRunning: Boolean = false,

    val currentStep : Int = 0,


) {
    // 1. 현재 운동 이름 (UI 표시용)
    val currentExerciseLabel: String
        get() = when (sessionMode) {
            is AiSessionMode.DurationMode -> "러닝"
            is AiSessionMode.AiRepMode -> sessionMode.exerciseType.label
        }

    // ✅ [New] 현재 진행 값 (예: "05:00" 또는 "12")
    val currentProgressValue: String
        get() = when (sessionMode) {
            is AiSessionMode.DurationMode -> formatTime(sessionMode.currentSeconds)
            is AiSessionMode.AiRepMode -> {
                if (sessionMode.exerciseType.isTimeBased) formatTime(sessionMode.currentCount)
                else sessionMode.currentCount.toString()
            }
        }

    // ✅ [New] 전체 목표 값 (예: "30:00" 또는 "15")
    val totalTargetValue: String
        get() = when (sessionMode) {
            is AiSessionMode.DurationMode -> formatTime(sessionMode.targetSeconds)
            is AiSessionMode.AiRepMode -> {
                if (sessionMode.exerciseType.isTimeBased) formatTime(sessionMode.targetCount)
                else sessionMode.targetCount.toString()
            }
        }

    // ✅ [Update] 기존 라벨은 위 두 변수를 조합해서 유지 (기존 UI 호환용)
    // 결과: "05:00 / 30:00" 또는 "12 / 15"
    val progressLabel: String
        get() = "$currentProgressValue / $totalTargetValue"


    // (내부 사용 헬퍼) 초 -> MM:SS 변환
    private fun formatTime(seconds: Int): String {
        return "%02d:%02d".format(seconds / 60, seconds % 60)
    }

}