package com.hye.domain.usecase.recommend

import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.repository.AgentBriefingRepository
import javax.inject.Inject

class GenerateAgentBriefingUseCase @Inject constructor(
    private val repository: AgentBriefingRepository
) {
    suspend operator fun invoke(profile: UserProfile, missions: List<Mission>): String {
        if (missions.isEmpty()) return "현재 하달할 수 있는 새로운 작전이 없습니다."

        return repository.generateBriefing(profile, missions)
    }
}