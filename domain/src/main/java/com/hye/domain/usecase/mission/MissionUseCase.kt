package com.hye.domain.usecase.mission

import javax.inject.Inject

data class MissionUseCase @Inject constructor(
    val insertMission : InsertMissionUseCase,
    val getMissionList : GetMissionListUseCase,
    val getMission : GetMissionUseCase,
    val getMissionRecords: GetMissionRecordsUseCase,
    val updateMissionRecord: UpdateMissionRecordUseCase
)