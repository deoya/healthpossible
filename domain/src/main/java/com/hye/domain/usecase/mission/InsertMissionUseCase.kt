package com.hye.domain.usecase.mission

import com.hye.domain.common.ExecutionResult
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.repository.MissionRepository
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertMissionUseCase @Inject constructor(
    private val repository: MissionRepository
) {
    operator fun invoke(mission: Mission) : Flow<MissionResult<ExecutionResult>> = repository.insertMission(mission)
}

