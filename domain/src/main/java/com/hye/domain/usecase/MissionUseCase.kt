package com.hye.domain.usecase

import com.hye.domain.usecase.mission.GetMissionListUseCase
import com.hye.domain.usecase.mission.GetMissionRecordsUseCase
import com.hye.domain.usecase.mission.GetMissionUseCase
import com.hye.domain.usecase.mission.InsertMissionUseCase
import com.hye.domain.usecase.mission.UpdateMissionRecordUseCase
import javax.inject.Inject

data class MissionUseCase @Inject constructor(
    val insertMission : InsertMissionUseCase,
    val getMissionList : GetMissionListUseCase,
    val getMission : GetMissionUseCase,
    val getMissionRecords: GetMissionRecordsUseCase,
    val updateMissionRecord: UpdateMissionRecordUseCase
)