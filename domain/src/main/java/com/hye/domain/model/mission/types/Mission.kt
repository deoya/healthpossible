package com.hye.domain.model.mission.types

import java.time.LocalTime

sealed interface Mission {
    val id: String
    val title: String
    val memo: String?
    val notificationTime: LocalTime?

    // 🔥 1. 요일(Set<DayOfWeek>) 대신 '주간 목표 횟수(1~7)'로 변경
    val weeklyTargetCount: Int

    // 🔥 2. 이 미션이 어떤 주차(월~일)에 속하는지 식별하는 값 (예: "2026-W09", 혹은 startDate "2026-03-02")
    // null 이면 이건 이번 주 미션이 아니라 보관함에 있는 '템플릿'임을 의미합니다.
    val weekIdentifier: String?

    // 🔥 3. 템플릿 저장 여부 (5번 요구사항)
    val isTemplate: Boolean

    fun updateCommon(
        title: String = this.title,
        weeklyTargetCount: Int = this.weeklyTargetCount, // 🔥 변경됨
        memo: String? = this.memo,
        notificationTime: LocalTime? = this.notificationTime
    ): Mission
}
sealed interface MissionReminder {
    object None : MissionReminder
    data class Interval(val minutes: Int) : MissionReminder
    data class SpecificTime(val times: List<LocalTime>) : MissionReminder
    object AlwaysOnDisplay : MissionReminder
}

enum class DayOfWeek {
    MON, TUE, WED, THU, FRI, SAT, SUN
}

enum class MissionType(val label: String) {
    EXERCISE("운동"),
    DIET("식단"),
    ROUTINE("상시"),
    RESTRICTION("제한")
}

val Mission.type: MissionType
    get() = when (this) {
        is ExerciseMission -> MissionType.EXERCISE
        is DietMission -> MissionType.DIET
        is RoutineMission -> MissionType.ROUTINE
        is RestrictionMission -> MissionType.RESTRICTION
    }

