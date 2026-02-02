package com.hye.domain.model.mission.types

import java.time.LocalTime

// 1. 운동 습관 (Exercise)
data class ExerciseMission(
    override val id: String,
    override val title: String,
    override val days: Set<DayOfWeek>,
    override val notificationTime: LocalTime?,
    override val memo: String? = null,

    // 특화 설정
    val targetValue: Int,      // 목표 수치 (예: 30)
    val unit: ExerciseRecordMode,      // 단위 (예: TIME -> 30분)
    val useSupportAgent: Boolean = false,        // 자세교정 AI 및 만보기 사용 여부

    val selectedExercise: String? = null
) : Mission

enum class ExerciseRecordMode(val label: String, val agentTask: String) {
    RUNNING("러닝", "걸음 수"),
    SELECTED("종목 선택", "자세")
}


enum class ExerciseAgentType {
    AI_POSTURE, RUNNING
}

//Todo: target을 별도 data class로 분리 해서 커스텀 할 수 있게 하기 - ExercisePreset
enum class AiExerciseType(
    val label: String, val description: String,
    val defaultTarget: Int,  // 실제 목표 수치 (예: 15 or 60)
    val isTimeBased: Boolean // 시간 측정인지 횟수 측정인지 구분
) {
    SQUAT("스쿼트", "15회", 15, false),
    LUNGE("런지", "양쪽 10회", 10, false),
    PLANK("플랭크", "60초", 60, true),
    SIDE_LUNGE("사이드 런지", "양쪽 12회", 12, false);
}

sealed interface AiSessionMode {

    // 1. [RUNNING 모드] -> 시간 중심 (목표 시간은 사용자가 설정한 값)
    data class DurationMode(
        val targetSeconds: Int = 0,     // 목표 시간 (초)
        val currentSeconds: Int = 0     // 현재 진행 시간
    ) : AiSessionMode

    // 2. [SELECTED 모드] -> AI 종목 중심 (목표는 AiExerciseType의 defaultTarget 사용)
    data class AiRepMode(
        val exerciseType: AiExerciseType, // 선택된 운동
        val targetCount: Int,             // 목표 (Enum에서 가져옴)
        val currentCount: Int = 0         // 현재 진행 (횟수 or 초)
    ) : AiSessionMode
}
