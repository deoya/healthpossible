package com.hye.mission.ui.state

import com.hye.domain.model.mission.MissionWithRecord
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.DayOfWeek
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.MissionType
import com.hye.domain.model.mission.types.RestrictionType
import java.time.LocalTime

data class MissionState (
    //공통
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // 미션 정보
    val missions: List<MissionWithRecord> = emptyList(),
    val totalMissionsCount: Int = 0,
    val completedMissionsCount: Int = 0,

    val inputMission: Mission? = null,

    // 입력 관련
    val selectedExerciseType: String = "",
    val isBottomSheetOpen: Boolean = false,
    // --- 공통 입력란 ---
    val selectedCategory: MissionType = MissionType.EXERCISE,
    val titleInput: String = "",
    val daysInput: Set<DayOfWeek> = emptySet(),
    val memoInput: String = "",
    val notificationTimeInput: LocalTime? = null,

    // --- 운동(Exercise) 특화 입력란 ---
    val exerciseUnitInput: ExerciseRecordMode = ExerciseRecordMode.SELECTED,
    val selectedExerciseTypeInput: AiExerciseType? = null,
    val exerciseTargetInput: Int? = null, // 빈칸을 위해 null 허용
    val useSupportAgentInput: Boolean = false,

    // --- 식단(Diet) 특화 입력란 ---
    val dietRecordMethodInput: DietRecordMethod = DietRecordMethod.PHOTO,

    // --- 상시(Routine) 특화 입력란 ---
    val routineDailyTargetInput: Int? = null,
    val routineStepAmountInput: Int? = null,
    val routineUnitLabelInput: String = "",

    // --- 제한(Restriction) 특화 입력란 ---
    val restrictionTypeInput: RestrictionType = RestrictionType.CHECK,
    val restrictionMaxMinutesInput: Int? = null

    )