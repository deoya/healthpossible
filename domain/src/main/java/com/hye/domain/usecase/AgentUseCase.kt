package com.hye.domain.usecase

import com.hye.domain.usecase.recommend.GenerateAgentBriefingUseCase
import javax.inject.Inject

data class AgentUseCase @Inject constructor (
    val generateAgentBriefing: GenerateAgentBriefingUseCase
)