package com.hye.mission.ui.screen

import ToastHelper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.domain.model.mission.DayOfWeek
import com.hye.domain.model.mission.ExerciseMission
import com.hye.domain.model.mission.ExerciseUnit
import com.hye.mission.ui.components.mission.MissionCreationContent
import com.hye.mission.ui.components.mission.TopBarAction
import com.hye.mission.ui.model.MissionState
import com.hye.mission.ui.model.MissionCreationViewModel
import com.hye.shared.theme.AppTheme

@Composable
fun MissionCreationScreen(
    viewModel: MissionCreationViewModel = hiltViewModel(),
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
) {
    val uiState by viewModel.uiStatus.collectAsStateWithLifecycle()
    var inputMission =  uiState.inputMission


    DisposableEffect(inputMission?.title) {

        val isTitleValid = inputMission != null && inputMission.title.isNotBlank()

        setTopBarActions({
            TopBarAction(
                onClick = { viewModel.insertMission() },
                enabled = isTitleValid,
                label = "저장",
                color = if (isTitleValid) AppTheme.colors.mainColor else Color.LightGray
            )
        })
        onDispose { setTopBarActions(null) }
    }

    LaunchedEffect(uiState.isInserted) {
        if(uiState.isInserted){
            ToastHelper.show(uiState.userMessage.toString())
            viewModel.resetMissionState()
        }
    }

    MissionCreationContent(
        uiState = uiState,
        // 공통
        onTitleChange = viewModel::updateTitle,
        onDayToggle = viewModel::toggleDay,
        onCategorySelected = viewModel::startCreation,
        // 운동
        onExerciseUnitChange = viewModel::updateExerciseUnit,
        onExerciseTargetChange = viewModel::updateExerciseTarget,
        onExerciseTimerToggle = viewModel::toggleExerciseTimer,
        // 식단
        onDietMethodChange = viewModel::updateDietRecordMethod,
        // 루틴
        onRoutineUnitLabelChange = viewModel::updateRoutineUnitLabel,
        onRoutineTotalChange = viewModel::updateRoutineTotalTarget,
        onRoutineStepChange = viewModel::updateRoutineStepAmount,
        // 제한
        onRestrictionTypeChange = viewModel::updateRestrictionType,
        onRestrictionTimeChange = viewModel::updateRestrictionTime,
        // 태그
        onTagAdd = viewModel::addTag,
        onTagRemove = viewModel::removeTag
    )
}

@Preview(showBackground = true, name = "운동 미션 생성 미리보기")
@Composable
fun MissionCreationExercisePreview() {
    var exerciseTargetString by remember { mutableStateOf("30") }
    var missionState by remember {
        mutableStateOf(
            ExerciseMission(
                id = "preview",
                title = "미리보기 운동",
                days = setOf(DayOfWeek.MON, DayOfWeek.WED, DayOfWeek.FRI),
                notificationTime = null,
                tags = listOf("건강", "아침"),
                targetValue = 30,
                unit = ExerciseUnit.TIME,
                useTimer = true
            )
        )
    }
    MissionCreationContent(
        uiState = MissionState(
            inputMission = missionState,
        ),
        // 공통 속성 업데이트 콜백
        onTitleChange = { newTitle ->
            missionState = missionState.copy(title = newTitle)
        },
        onDayToggle = { day ->
            val newDays = if (missionState.days.contains(day)) {
                missionState.days - day
            } else {
                missionState.days + day
            }
            missionState = missionState.copy(days = newDays)
        },
        onCategorySelected = { },

        onExerciseUnitChange = { newUnit ->
            missionState = missionState.copy(unit = newUnit)
        },
        onExerciseTargetChange = { newTargetString ->
            exerciseTargetString = newTargetString

            val newFloatValue = newTargetString.toIntOrNull() ?: 0
            missionState = missionState.copy(targetValue = newFloatValue)
        },
        onExerciseTimerToggle = { useTimer ->
            missionState = missionState.copy(useTimer = useTimer)
        },

        // 태그 업데이트 콜백
        onTagAdd = { tag ->
            if (tag.isNotBlank() && !missionState.tags.contains(tag)) {
                val newTags = missionState.tags + tag
                missionState = missionState.copy(tags = newTags)
            }
        },
        onTagRemove = { tag ->
            val newTags = missionState.tags - tag
            missionState = missionState.copy(tags = newTags)
        },

        onDietMethodChange = {},
        onRoutineUnitLabelChange = {},
        onRoutineTotalChange = {},
        onRoutineStepChange = {},
        onRestrictionTypeChange = {},
        onRestrictionTimeChange = {}
    )
}
