package com.hye.domain.model.mission

import java.time.LocalTime

sealed interface Mission {
    val id: String
    val title: String
    val days: Set<DayOfWeek> // 수행 요일
    val notificationTime: LocalTime? // 알림 시간 (null이면 알림 없음)
    val tags: List<String> // 태그
}
fun Mission.copyCommon(
    title: String = this.title,
    days: Set<DayOfWeek> = this.days,
    tags: List<String> = this.tags,
    notificationTime: LocalTime? = this.notificationTime
): Mission {
    return when (this) {
        is ExerciseMission -> copy(title = title, days = days, tags = tags, notificationTime = notificationTime)
        is DietMission -> copy(title = title, days = days, tags = tags, notificationTime = notificationTime)
        is RoutineMission -> copy(title = title, days = days, tags = tags, notificationTime = notificationTime)
        is RestrictionMission -> copy(title = title, days = days, tags = tags, notificationTime = notificationTime)
    }
}

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
    val useTimer: Boolean        // 앱 내 타이머 사용 여부
) : Mission

// 2. 식단 습관 (Meal)
data class DietMission(
    override val id: String,
    override val title: String,
    override val days: Set<DayOfWeek>,
    override val notificationTime: LocalTime?,
    override val tags: List<String> = emptyList(),

    // 특화 설정
    val recordMethod: DietRecordMethod // 기록 방식 (사진/글/체크)
) : Mission

// 3. 상시 습관 (Routine/Water)
data class RoutineMission(
    override val id: String,
    override val title: String,
    override val days: Set<DayOfWeek>,
    override val notificationTime: LocalTime?,
    override val tags: List<String> = emptyList(),

    // 특화 설정
    val dailyTargetAmount: Int,  // 하루 총 목표량 (예: 2000ml)
    val amountPerStep: Int,      // 1회 클릭당 증가량 (예: 250ml)
    val unitLabel: String        // 단위 표기 (예: "ml", "잔")
) : Mission

// 4. 제한 습관 (Restriction)
data class RestrictionMission(
    override val id: String,
    override val title: String,
    override val days: Set<DayOfWeek>,
    override val notificationTime: LocalTime?,
    override val tags: List<String> = emptyList(),

    // 특화 설정
    val type: RestrictionType,   // 타이머형 vs 체크형
    val maxAllowedMinutes: Int?  // 허용 시간 (타이머형일 경우만 사용, null 가능)
) : Mission


// 운동 측정 단위
enum class ExerciseUnit(val label: String) {
    TIME("시간(분)"),
    DISTANCE("거리(km)"),
    COUNT("횟수"),
    SET("세트")
}

// 식단 기록 방식
enum class DietRecordMethod {
    PHOTO,  // 사진 인증
    TEXT,   // 텍스트 기록
    CHECK   // 단순 수행 체크
}

// 제한(금지) 방식
enum class RestrictionType {
    TIMER,  // 참은 시간 측정 (예: 단식)
    CHECK   // 하루 성공/실패 체크 (예: 금연)
}

enum class MissionCategory(val label: String) {
    EXERCISE("운동"),
    DIET("식단"),
    ROUTINE("상시"),
    RESTRICTION("제한")
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