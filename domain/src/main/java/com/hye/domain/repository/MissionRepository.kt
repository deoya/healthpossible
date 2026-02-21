package com.hye.domain.repository

import com.hye.domain.common.ExecutionResult
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MissionRepository {

    fun getMissionList(): Flow<MissionResult<List<Mission>>>

    suspend fun getMission(id: String): MissionResult<Mission?>

    fun updateMission(mission: Mission): Flow<MissionResult<ExecutionResult>>

    fun insertMission(mission: Mission): Flow<MissionResult<ExecutionResult>>

    fun deleteMission(missionId: String): Flow<MissionResult<ExecutionResult>>

    fun getMissionRecords(date: LocalDate): Flow<MissionResult<List<MissionRecord>>>

    fun updateMissionRecord(record: MissionRecord): Flow<MissionResult<ExecutionResult>>
}
