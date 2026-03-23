package com.hye.mission.ui.state

import com.hye.domain.model.mission.RecommendedMission

data class RecommendUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val recommendations: List<RecommendedMission> = emptyList(),

    val agentMessage: String = "요원님의 데이터를 분석하여\n 작전을 수립 중입니다",

    val originalRecommendations: List<RecommendedMission> = emptyList(), // 되돌리기를 위한 원본 미션 백업
    val originalAgentMessage: String = "",                               // 되돌리기를 위한 원본 브리핑 백업

    val isAdjustmentMode: Boolean = false,                               // 미션 조정 입력창이 열려있는지 여부
    val adjustmentInput: String = "",                                    // 요원님이 입력창에 타이핑 중인 텍스트
    val isAdjusted: Boolean = false

)