package com.hye.domain.model.mission.types

import java.time.LocalTime

// 1. 운동 습관 (Exercise)
data class ExerciseMission(
    override val id: String,
    override val title: String,
    override val days: Set<DayOfWeek>,
    override val notificationTime: LocalTime?,
    override val tags: List<String> = emptyList(),

    // 특화 설정
    val targetValue: Int,      // 목표 수치 (예: 30)
    val unit: ExerciseUnit,      // 단위 (예: TIME -> 30분)
    val useSupportAgent: Boolean = false,        // 자세교정 AI 및 만보기 사용 여부
) : Mission

// 운동 측정 단위
enum class ExerciseUnit(val label: String) {
    TIME("시간(분)"),
    DISTANCE("거리(km)"),
    COUNT("횟수"),
    SET("세트")
}