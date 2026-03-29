package com.hye.domain.result

import com.hye.domain.model.agent.AgentResponseType
import com.hye.domain.model.mission.RecommendedMission

data class RecommendationPipelineResult(
    val type: AgentResponseType,
    val recommendations: List<RecommendedMission>,
    val briefingMessage: String
)