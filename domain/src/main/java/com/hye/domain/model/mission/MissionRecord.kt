package com.hye.domain.model.mission

import com.hye.domain.model.mission.types.Mission

// 오늘의 수행 기록을 담는 엔티티
data class MissionRecord(
    val id: String = "",          // 기록 ID
    val missionId: String,        // 연결된 미션 ID
    val date: String,             // 날짜 (YYYY-MM-DD)
    val progress: Int = 0,        // 현재 수행량 (예: 500ml, 15분, 1회)
    val isCompleted: Boolean = false, // 완료 여부
    val completedAt: String? = null // 완료 시각
)


// UI에 보여주기 위해 미션 정보와 기록을 합친 모델
data class MissionWithRecord(
    val mission: Mission,
    val record: MissionRecord? // 기록이 없으면(아직 시작 안함) null
) {
    // 현재 진행률 계산 (UI 로직 편의성)
    val currentProgress: Int get() = record?.progress ?: 0
    val isCompleted: Boolean get() = record?.isCompleted ?: false
}