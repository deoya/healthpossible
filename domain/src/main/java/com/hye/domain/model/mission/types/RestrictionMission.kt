package com.hye.domain.model.mission.types

import java.time.LocalTime

// 4. 제한 습관 (Restriction)
data class RestrictionMission(
    override val id: String,
    override val title: String,
    override val days: Set<DayOfWeek>,
    override val notificationTime: LocalTime?,
    override val memo: String? = null,

    // 특화 설정
    val type: RestrictionType,   // 타이머형 vs 체크형
    val maxAllowedMinutes: Int?  // 허용 시간 (타이머형일 경우만 사용, null 가능)

) : Mission

// 제한(금지) 방식
enum class RestrictionType {
    TIMER,  // 참은 시간 측정 (예: 단식)
    CHECK   // 하루 성공/실패 체크 (예: 금연)
}