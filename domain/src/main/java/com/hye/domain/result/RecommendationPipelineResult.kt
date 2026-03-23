package com.hye.domain.result

import com.hye.domain.model.mission.RecommendedMission

data class RecommendationPipelineResult(
    val recommendations: List<RecommendedMission>,
    val briefingMessage: String
)