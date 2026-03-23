package com.hye.domain.usecase.recommend

import com.hye.domain.model.agent.AgentRecommendationData
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.repository.AgentBriefingRepository
import com.hye.domain.result.AgentRecommendationResult
import javax.inject.Inject

class GenerateAgentBriefingUseCase @Inject constructor(
    private val repository: AgentBriefingRepository
) {
    suspend operator fun invoke(profile: UserProfile, missions: List<Mission>): AgentRecommendationResult<AgentRecommendationData> {

        if (missions.isEmpty()) {
            return AgentRecommendationResult.Error(
                exception = IllegalStateException("No available missions"),
                fallbackBriefing = "현재 하달할 수 있는 새로운 작전이 없습니다."
            )
        }

        return repository.generateRecommendation(profile, missions)
    }
}