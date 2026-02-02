package com.hye.domain.model.mission.types

import java.time.LocalTime

// 2. 식단 습관 (Meal)
data class DietMission(
    override val id: String,
    override val title: String,
    override val days: Set<DayOfWeek>,
    override val notificationTime: LocalTime?,
    override val memo: String? = null,

    // 특화 설정
    val recordMethod: DietRecordMethod // 기록 방식 (사진/글/체크)
) : Mission

// 식단 기록 방식
enum class DietRecordMethod {
    PHOTO,  // 사진 인증
    TEXT,   // 텍스트 기록
    CHECK   // 단순 수행 체크
}