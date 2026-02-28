package com.hye.domain.model.mission.types

import java.time.LocalTime

// 3. 상시 습관 (Routine/Water)
data class RoutineMission(
    override val id: String,
    override val title: String,
    override val notificationTime: LocalTime?,
    override val memo: String? = null,
    override val weeklyTargetCount: Int,
    override val weekIdentifier: String? = null,
    override val isTemplate: Boolean = false,
    // 특화 설정
    val dailyTargetAmount: Int,  // 하루 총 목표량 (예: 2000ml)
    val amountPerStep: Int,      // 1회 클릭당 증가량 (예: 250ml)
    val unitLabel: String        // 단위 표기 (예: "ml", "잔")
) : Mission{
    override fun updateCommon(
        title: String,
        weeklyTargetCount: Int,
        memo: String?,
        notificationTime: LocalTime?
    ): Mission = copy(
        title = title,
        weeklyTargetCount = weeklyTargetCount,
        memo = memo,
        notificationTime = notificationTime
    )
}