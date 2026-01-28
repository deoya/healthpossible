package com.hye.domain.repository

import com.hye.domain.common.ExecutionResult
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.flow.Flow

interface MissionRepository {

    fun getMissionList(): Flow<MissionResult<List<Mission>>>

    suspend fun getMission(id: String): MissionResult<Mission?>

    suspend fun updateMission(id: String): MissionResult<Mission?>

    fun insertMission(mission: Mission) : Flow<MissionResult<ExecutionResult>>

    suspend fun deleteMission(habit: Mission) : MissionResult<Unit>

    // [추가] 오늘 날짜의 수행 기록들을 실시간으로 가져오기
    fun getMissionRecords(date: String): Flow<MissionResult<List<MissionRecord>>>

    // [추가] 수행 기록 업데이트 (진행도 증가, 완료 처리 등)
    fun updateMissionRecord(record: MissionRecord): Flow<MissionResult<ExecutionResult>>
}