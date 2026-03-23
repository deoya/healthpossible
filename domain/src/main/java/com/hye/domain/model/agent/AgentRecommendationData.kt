package com.hye.domain.model.agent

// AI 응답 데이터를 담을 홀더
data class AgentRecommendationData(
    val selectedMissionIds: List<String>,
    val briefing: String
)