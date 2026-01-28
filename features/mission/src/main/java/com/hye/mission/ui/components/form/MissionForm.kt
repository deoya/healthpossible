package com.hye.mission.ui.components.form

import androidx.compose.runtime.Composable
import com.hye.domain.model.mission.MissionActionsByType
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RoutineMission


val Int.emptyIfZero : String
    get() = if(this == 0) "" else this.toString()


@Composable
fun MissionForm(
    mission: Mission,
    actions: MissionActionsByType
) {
    when (mission) {
        is ExerciseMission -> MissionForm(mission, actions)
        is DietMission -> MissionForm(mission, actions)
        is RoutineMission -> MissionForm(mission, actions)
        is RestrictionMission -> MissionForm(mission, actions)
    }
}


@Composable
fun MissionForm(
    mission: ExerciseMission,
    actions: MissionActionsByType
) = ExerciseSettingForm(
    selectedUnit = mission.unit,
    onUnitSelected = actions.onExerciseUnitChange,
    // UI에서는 String으로 표시 (0일 경우 빈값)
    targetValue = mission.targetValue.emptyIfZero,
    onTargetValueChange = actions.onExerciseTargetChange,
    useSupportAgent = mission.useSupportAgent,
    onUseSupportAgentChange = actions.onExerciseSupportAgentToggle
)

@Composable
fun MissionForm(
    mission: DietMission,
    actions: MissionActionsByType
) = DietSettingForm(
    recordMethod = mission.recordMethod,
    onMethodSelected = actions.onDietMethodChange
)

@Composable
fun MissionForm(
    mission: RoutineMission,
    actions: MissionActionsByType
) = RoutineSettingForm(
    totalTarget = mission.dailyTargetAmount.emptyIfZero,
    onTotalTargetChange = actions.onRoutineTotalChange,
    stepAmount = mission.amountPerStep.emptyIfZero,
    onStepAmountChange = actions.onRoutineStepChange,
    unitLabel = mission.unitLabel,
    onUnitLabelChange = actions.onRoutineUnitLabelChange
)

@Composable
fun MissionForm(
    mission: RestrictionMission,
    actions: MissionActionsByType
) = RestrictionSettingForm(
    type = mission.type,
    onTypeSelected = actions.onRestrictionTypeChange,
    // 분 -> 시간 변환하여 표시
    maxAllowedTime = mission.maxAllowedMinutes?.div(60)?.toString()
        ?: "",
    onMaxTimeChange = actions.onRestrictionTimeChange
)
