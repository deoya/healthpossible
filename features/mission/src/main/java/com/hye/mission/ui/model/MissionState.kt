package com.hye.mission.ui.model

import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.MissionWithRecord
import com.hye.domain.result.MissionResult

data class MissionState (
    val missions: List<MissionWithRecord> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val totalMissionsCount: Int = 0,
    val completedMissionsCount: Int = 0,

    val userMessage: String? = null,

    val selectMission: MissionResult<Mission> = MissionResult.NoConstructor,
    val deleteMission: MissionResult<Unit> = MissionResult.NoConstructor,
    val inputMission: Mission? = null,
    val isInserted: Boolean = false,

    val isBottomSheetOpen: Boolean = false,
    val selectedExerciseType: String = ""

)