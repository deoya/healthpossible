package com.hye.domain.usecase.mission

import com.hye.domain.model.mission.Mission
import com.hye.domain.repository.MissionRepository
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMissionListUseCase @Inject constructor(
    private val repository: MissionRepository
){
    operator fun invoke(): Flow<MissionResult<List<Mission>>> {
        return repository.getMissionList()
    }
}