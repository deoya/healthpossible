package com.hye.domain.repository

import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.profile.UserProfile

interface AgentBriefingRepository {
    suspend fun generateBriefing(profile: UserProfile, missions: List<Mission>): String
}