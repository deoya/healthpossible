package com.hye.domain.repository

import com.hye.domain.model.agent.AgentRecommendationData
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.result.AgentRecommendationResult

interface AgentBriefingRepository {
    suspend fun generateRecommendation(
        profile: UserProfile,
        missions: List<Mission>,
        guidelineText: String? = null
    ): AgentRecommendationResult<AgentRecommendationData>
}