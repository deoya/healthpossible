package com.hye.mission.ui.model

import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.MissionWithRecord
import com.hye.domain.result.MissionResult

data class MissionState (
    //공통
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userMessage: String? = null,
    // 미션 정보
    val missions: List<MissionWithRecord> = emptyList(),
    val totalMissionsCount: Int = 0,
    val completedMissionsCount: Int = 0,

    val selectMission: MissionResult<Mission> = MissionResult.NoConstructor,
    val deleteMission: MissionResult<Unit> = MissionResult.NoConstructor,
    val inputMission: Mission? = null,
    // 입력 관련
    val isInserted: Boolean = false,
    val selectedExerciseType: String = "",

    val isBottomSheetOpen: Boolean = false,


)