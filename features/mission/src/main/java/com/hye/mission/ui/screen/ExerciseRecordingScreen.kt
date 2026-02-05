package com.hye.mission.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.ExerciseAgentType
import com.hye.features.mission.R
import com.hye.mission.ui.components.recording.exercise.posture.AiPostureSession
import com.hye.mission.ui.components.recording.exercise.running.RunningSession
import com.hye.mission.ui.state.RecordState
import com.hye.mission.ui.viewmodel.MissionRecordingViewModel
import com.hye.shared.base.BaseScreenTemplate
import com.hye.shared.util.text

@Composable
fun ExerciseRecordingScreen(
    viewModel: MissionRecordingViewModel = hiltViewModel(),
    missionId: String,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect (missionId) {
        viewModel.loadMission(missionId)
    }

    BaseScreenTemplate(
        viewModel = viewModel,
        screenName = R.string.mission_exercise_recording_screen.text,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
    ){
        ExerciseSessionContent(
            uiState = uiState,
            onToggleBottomSheet = viewModel::toggleBottomSheet,
            onSelectExerciseType = viewModel::selectExercise,
            onToggleTimer = viewModel::toggleTimer,
            onIncreaseCount = viewModel::increaseCount,
            onUpdateFeedback = viewModel::updateFeedback
        )
    }
}

// 2. Preview를 위한 순수 UI (Stateless)
@Composable
fun ExerciseSessionContent(
    uiState: RecordState,
    onToggleBottomSheet: (Boolean) -> Unit = {},
    onSelectExerciseType: (AiExerciseType) -> Unit = {},
    onToggleTimer: () -> Unit = {},
    onIncreaseCount:()->Unit = {},
    onUpdateFeedback: (String) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState.exerciseAgentType) {
            ExerciseAgentType.AI_POSTURE -> AiPostureSession(
                state = uiState,
                onToggleBottomSheet = onToggleBottomSheet,
                onSelectExerciseType = onSelectExerciseType,
                onIncreaseCount = onIncreaseCount,
                onUpdateFeedback = onUpdateFeedback
            )

            ExerciseAgentType.RUNNING -> RunningSession(
                state = uiState,
                onToggleTimer = onToggleTimer,
            )
        }
    }
}

@Preview
@Composable
fun PreviewExerciseSessionAi() {
    ExerciseSessionContent(
        uiState = RecordState(exerciseAgentType = ExerciseAgentType.AI_POSTURE),
    )
}