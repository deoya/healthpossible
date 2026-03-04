package com.hye.mission.ui.state

import com.hye.domain.model.mission.RecommendedMission

data class RecommendUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val recommendations: List<RecommendedMission> = emptyList(),

    val agentMessage: String = "요원님의 데이터를 분석하여 작전을 수립 중입니다..."
)