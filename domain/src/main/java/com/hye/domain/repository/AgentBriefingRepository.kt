package com.hye.domain.repository

import com.hye.domain.model.agent.AgentRecommendationData
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.result.AgentRecommendationResult

interface AgentBriefingRepository {
    suspend fun generateRecommendation(
        profile: UserProfile,
        safeMissions: List<Mission>,
        activeMissions: List<Mission>, // 현재 수행 중인 작전 목록
        guidelineText: String? = null,
        userFeedback: String? = null
    ): AgentRecommendationResult<AgentRecommendationData>
}