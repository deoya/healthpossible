package com.hye.mission.ui.components.form

import androidx.compose.runtime.Composable
import com.hye.domain.model.mission.MissionActionsByType
import com.hye.domain.model.mission.types.MissionType
import com.hye.mission.ui.components.form.type.DietSettingForm
import com.hye.mission.ui.components.form.type.RestrictionSettingForm
import com.hye.mission.ui.components.form.type.RoutineSettingForm
import com.hye.mission.ui.components.form.type.exercise.ExerciseSettingForm
import com.hye.mission.ui.state.MissionState

val Int?.emptyIfNull: String
    get() = this?.toString() ?: ""

@Composable
fun MissionForm(
    missionCategory: MissionType,
    uiState: MissionState,
    actions: MissionActionsByType
) {
    when (missionCategory) {
        MissionType.EXERCISE -> {
            ExerciseSettingForm(
                selectedUnit = uiState.exerciseUnitInput,
                onUnitSelected = actions.onExerciseUnitChange,
                targetValue = uiState.exerciseTargetInput.emptyIfNull,
                onTargetValueChange = actions.onExerciseTargetChange,
                useSupportAgent = uiState.useSupportAgentInput,
                onUseSupportAgentChange = actions.onExerciseSupportAgentToggle,
                selectedExerciseName = uiState.selectedExerciseTypeInput?.label ?: "",
                onExerciseTypeClick = { actions.onChangeBottomSheetState(true) }
            )
        }
        MissionType.DIET -> {
            DietSettingForm(
                recordMethod = uiState.dietRecordMethodInput,
                onMethodSelected = actions.onDietMethodChange
            )
        }
        MissionType.ROUTINE -> {
            RoutineSettingForm(
                totalTarget = uiState.routineDailyTargetInput.emptyIfNull,
                onTotalTargetChange = actions.onRoutineTotalChange,
                stepAmount = uiState.routineStepAmountInput.emptyIfNull,
                onStepAmountChange = actions.onRoutineStepChange,
                unitLabel = uiState.routineUnitLabelInput,
                onUnitLabelChange = actions.onRoutineUnitLabelChange
            )
        }
        MissionType.RESTRICTION -> {
            RestrictionSettingForm(
                type = uiState.restrictionTypeInput,
                onTypeSelected = actions.onRestrictionTypeChange,
                maxAllowedTime = uiState.restrictionMaxMinutesInput?.div(60)?.toString() ?: "",
                onMaxTimeChange = actions.onRestrictionTimeChange
            )
        }
    }
}