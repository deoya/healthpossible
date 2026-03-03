package com.hye.domain.model.mission

import com.hye.domain.model.mission.types.Mission
import java.time.LocalDate

// 1. 단일 수행 기록 (기존과 유사하지만 역할이 '특정 날짜의 1회 수행분'으로 명확해짐)
data class MissionRecord(
    val id: String = "",
    val missionId: String,
    val date: LocalDate,          // 수행한 날짜
    val progress: Int = 0,        // 현재 수행량 (예: 500ml, 15분, 1회)
    val isCompleted: Boolean = false, // 이 날짜의 목표치를 완전히 채웠는지 여부
    val completedAt: String? = null
)

// 🔥 2. UI에 보여주기 위한 '주간 미션 통합 상태' 모델 (이름 변경 및 확장)
data class WeeklyMissionState(
    val mission: Mission,
    val todayRecord: MissionRecord?,        // 오늘자 기록 (진행 중이거나 완료했으면 존재)
    val weeklyRecords: List<MissionRecord>  // 이번 주(월~일)에 생성된 모든 기록들
) {
    // UI 로직 편의성: 오늘 이 미션을 완료했는가? (체크 버튼 상태 표시용)
    val isDoneToday: Boolean
        get() = todayRecord?.isCompleted ?: false

    // UI 로직 편의성: 오늘의 진행률 (예: 물 마시기 500/2000ml 게이지 표시용)
    val todayProgress: Int
        get() = todayRecord?.progress ?: 0

    // 🔥 UI 로직 편의성: 이번 주에 총 몇 번이나 완료했는가? (예: 2 / 5 회)
    val completedCountThisWeek: Int
        get() = weeklyRecords.count { it.isCompleted }

    // 🔥 UI 로직 편의성: 이번 주 목표 횟수를 전부 채웠는가? (축하 효과, 완료 뱃지 표시용)
    val isWeeklyTargetAchieved: Boolean
        get() = completedCountThisWeek >= mission.weeklyTargetCount
}