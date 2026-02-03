package com.hye.mission.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.ExerciseAgentType
import com.hye.mission.ui.components.recording.exercise.posture.AiPostureSession
import com.hye.mission.ui.components.recording.exercise.running.RunningSession
import com.hye.mission.ui.model.MissionRecordingViewModel
import com.hye.mission.ui.model.RecordState

@Composable
fun ExerciseRecordingScreen(
    viewModel: MissionRecordingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExerciseSessionContent(
        uiState = uiState,
        onToggleBottomSheet = viewModel::toggleBottomSheet,
        onSelectExerciseType = viewModel::selectExercise,
        onToggleTimer = viewModel::toggleTimer
    )
}

// 2. Preview를 위한 순수 UI (Stateless)
@Composable
fun ExerciseSessionContent(
    uiState: RecordState,
    onToggleBottomSheet: (Boolean) -> Unit = {},
    onSelectExerciseType: (AiExerciseType) -> Unit = {},
    onToggleTimer: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState.exerciseAgentType) {
            ExerciseAgentType.AI_POSTURE -> AiPostureSession(
                state = uiState,
                onToggleBottomSheet = onToggleBottomSheet,
                onSelectExerciseType = onSelectExerciseType,
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