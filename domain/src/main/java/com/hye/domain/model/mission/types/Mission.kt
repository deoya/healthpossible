package com.hye.domain.model.mission.types

import java.time.LocalTime

sealed interface Mission {
    val id: String
    val title: String
    val days: Set<DayOfWeek> // 수행 요일
    val notificationTime: LocalTime? // 알림 시간 (null이면 알림 없음)
    val memo: String?
}
fun Mission.copyCommon(
    title: String = this.title,
    days: Set<DayOfWeek> = this.days,
    memo: String? = this.memo,
    notificationTime: LocalTime? = this.notificationTime
): Mission {
    return when (this) {
        is ExerciseMission -> copy(title = title, days = days, memo = memo, notificationTime = notificationTime)
        is DietMission -> copy(title = title, days = days, memo = memo, notificationTime = notificationTime)
        is RoutineMission -> copy(title = title, days = days, memo = memo, notificationTime = notificationTime)
        is RestrictionMission -> copy(title = title, days = days, memo = memo, notificationTime = notificationTime)
    }
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

