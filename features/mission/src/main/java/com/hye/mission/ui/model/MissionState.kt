package com.hye.mission.ui.model

import com.hye.domain.model.mission.Mission
import com.hye.domain.result.MissionResult

data class MissionState (
    val missionList : MissionResult<List<Mission>> = MissionResult.Loading,

    val selectMission : MissionResult<Mission> = MissionResult.NoConstructor,

    val deleteMission : MissionResult<Unit> = MissionResult.NoConstructor,

    val isLoading: Boolean = false,

    //현재 작성 중인 미션 (임시 저장소)
    val inputMission: Mission? = null,

    val userMessage: String? = null,

    val isInserted: Boolean = false

)