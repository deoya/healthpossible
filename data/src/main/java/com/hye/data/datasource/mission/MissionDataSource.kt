package com.hye.data.datasource.mission

import com.hye.data.model.MissionDto
import com.hye.data.model.MissionRecordDto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MissionDataSource {

    // --- Mission Functions ---
    fun getMissions(): Flow<List<MissionDto>>
    suspend fun getMission(missionId: String): MissionDto?
    suspend fun addMission(missionDto: MissionDto)
    suspend fun updateMission(missionDto: MissionDto)
    suspend fun deleteMission(missionId: String)

    // --- Mission Record Functions ---
    fun getMissionRecords(date: LocalDate): Flow<List<MissionRecordDto>>
    suspend fun updateMissionRecord(recordDto: MissionRecordDto)
}
