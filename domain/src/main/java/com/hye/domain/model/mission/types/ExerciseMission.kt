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
enum class AiExerciseType(val label: String, val target: String) {
    SQUAT("스쿼트", "15회"),
    LUNGE("런지", "양쪽 10회"),
    PLANK("플랭크", "60초"),
    SIDE_LUNGE("사이드 런지", "양쪽 12회")
}

sealed interface AiSessionMode {

    // 모드 A: 시간 중심 (Unit == TIME) -> 루틴 없음, 운동 종류 하나만 선택해서 진행
    data class DurationMode(
        val currentExercise: AiExerciseType = AiExerciseType.SQUAT, // 현재 선택된 운동
        val targetSeconds: Int = 0,     // 목표 시간 (초)
        val currentSeconds: Int = 0     // 현재 진행 시간
    ) : AiSessionMode

    // 모드 B: 횟수/세트 중심 (Unit == SET/COUNT) -> 여러 단계의 루틴 존재
    data class RoutineMode(
        val steps: List<AiRoutineStep>, // 루틴 리스트
        val currentStepIndex: Int = 0   // 현재 진행 중인 단계 인덱스
    ) : AiSessionMode
}

// 루틴 스텝 데이터 (횟수 모드에서만 사용)
data class AiRoutineStep(
    val type: AiExerciseType,
    val targetCount: Int,
    val currentCount: Int = 0
)