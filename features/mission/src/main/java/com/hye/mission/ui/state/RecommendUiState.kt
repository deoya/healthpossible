package com.hye.mission.ui.state

import com.hye.domain.model.agent.AgentResponseType
import com.hye.domain.model.mission.RecommendedMission

data class RecommendUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val agentMessage: String = "",
    val recommendations: List<RecommendedMission> = emptyList(),

    val responseType: AgentResponseType = AgentResponseType.RECOMMEND,
    val originalResponseType: AgentResponseType = AgentResponseType.RECOMMEND, // 되돌리기용 백업

    val originalRecommendations: List<RecommendedMission> = emptyList(), // 되돌리기를 위한 원본 미션 백업
    val originalAgentMessage: String = "",                               // 되돌리기를 위한 원본 브리핑 백업

    val isAdjustmentMode: Boolean = false,                               // 미션 조정 입력창이 열려있는지 여부
    val adjustmentInput: String = "",                                    // 요원님이 입력창에 타이핑 중인 텍스트
    val isAdjusted: Boolean = false
)