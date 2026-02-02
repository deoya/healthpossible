package com.hye.domain.model.mission

import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.RestrictionType

// form 생성 시 편의성을 위함
data class MissionActionsByType(
    val onExerciseUnitChange: (ExerciseRecordMode) -> Unit,
    val onExerciseTargetChange: (String) -> Unit,
    val onExerciseSupportAgentToggle: (Boolean) -> Unit,
    val onChangeBottomSheetState : (Boolean) -> Unit,
    val onSelectExerciseType : (String) -> Unit,

    val onDietMethodChange: (DietRecordMethod) -> Unit,

    val onRoutineUnitLabelChange: (String) -> Unit,
    val onRoutineTotalChange: (String) -> Unit,
    val onRoutineStepChange: (String) -> Unit,

    val onRestrictionTypeChange: (RestrictionType) -> Unit,
    val onRestrictionTimeChange: (String) -> Unit,

)