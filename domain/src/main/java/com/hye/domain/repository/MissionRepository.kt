package com.hye.domain.repository

import com.hye.domain.common.ExecutionResult
import com.hye.domain.model.mission.Mission
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.flow.Flow

interface MissionRepository {

    fun getMissionList(): Flow<MissionResult<List<Mission>>>

    suspend fun getMission(id: String): MissionResult<Mission?>

    suspend fun updateMission(id: String): MissionResult<Mission?>

    fun insertMission(mission: Mission) : Flow<MissionResult<ExecutionResult>>

    suspend fun deleteMission(habit: Mission) : MissionResult<Unit>
}