package com.hye.domain.usecase.mission

import com.hye.domain.model.mission.types.Mission
import com.hye.domain.repository.MissionRepository
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMissionUseCase @Inject constructor(
    private val repository: MissionRepository
) {
    suspend operator fun invoke(id: String): MissionResult<Mission?> {
        return repository.getMission(id)
    }
}