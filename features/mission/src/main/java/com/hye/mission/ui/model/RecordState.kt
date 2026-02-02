package com.hye.mission.ui.model

import com.hye.domain.model.mission.types.AiSessionMode
import com.hye.domain.model.mission.types.ExerciseAgentType


// UI 상태 정의
data class RecordState(
    // 공통 정보
    val exerciseAgentType: ExerciseAgentType = ExerciseAgentType.AI_POSTURE,
    val feedbackMessage: String = "준비해주세요.",
    val isBottomSheetOpen: Boolean = false,

    // 세션 모드 (초기값은 임시로 DurationMode)
    val sessionMode: AiSessionMode = AiSessionMode.DurationMode()
) {
    // 1. 현재 운동 이름 (UI 표시용)
    val currentExerciseLabel: String
        get() = when (sessionMode) {
            is AiSessionMode.DurationMode -> "러닝"
            is AiSessionMode.AiRepMode -> sessionMode.exerciseType.label
        }

    // 2. 진행 상황 텍스트 (슬라이드 버튼 등에 사용)
    val progressLabel: String
        get() = when (sessionMode) {
            // A. 러닝 모드 -> "05:00 / 30:00" 형태 (시간)
            is AiSessionMode.DurationMode -> {
                val curr = sessionMode.currentSeconds
                val total = sessionMode.targetSeconds
                formatTimeProgress(curr, total)
            }

            // B. AI 종목 모드
            is AiSessionMode.AiRepMode -> {
                val type = sessionMode.exerciseType
                val curr = sessionMode.currentCount
                val total = sessionMode.targetCount

                if (type.isTimeBased) {
                    // 플랭크 등 시간 기반 -> "00:45 / 01:00"
                    formatTimeProgress(curr, total)
                } else {
                    // 스쿼트 등 횟수 기반 -> "12 / 15"
                    "$curr / $total"
                }
            }
        }

    // 시간 포맷팅 헬퍼 함수
    private fun formatTimeProgress(currentSec: Int, totalSec: Int): String {
        return "%02d:%02d / %02d:%02d".format(
            currentSec / 60, currentSec % 60,
            totalSec / 60, totalSec % 60
        )
    }
}