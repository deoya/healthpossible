package com.hye.mission.ui.components.recording.exercise.posture

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.ExerciseAgentType
import com.hye.mission.ui.components.recording.layout.BaseBottomBarContentLayout
import com.hye.mission.ui.components.recording.layout.BaseSessionLayout
import com.hye.mission.ui.components.recording.simulation.CameraSimulationView
import com.hye.mission.ui.state.RecordState

//Todo: 이 화면에서는 가로모드 대응 해놓을 것
@Composable
fun AiPostureSession(
    state: RecordState,
    onToggleBottomSheet: (Boolean) -> Unit,
    onSelectExerciseType: (AiExerciseType) -> Unit,
) {
    BaseSessionLayout(
        backgroundContent = {
            CameraSimulationView()
        },
        topBarContent = {
            //Todo :  CommonExecutionTopBar(onClose = anotherTypeMenu, state = state)
        },
        bottomBarContent = {
            BaseBottomBarContentLayout(
                content = {
                    AiPostureContent(
                    state = state,
                    onToggleBottomSheet = onToggleBottomSheet,
                )},
                bottomSheetContent = {
                    AiPostureBottomSheet(
                        state = state,
                        progressText = state.progressLabel,
                        onToggleBottomSheet = onToggleBottomSheet,
                        onSelectExercise = onSelectExerciseType
                    )
                }
            )
        }
    )
}

@Preview
@Composable
fun PreviewExerciseSessionAi2() {
    AiPostureSession(
        state = RecordState(
            exerciseAgentType = ExerciseAgentType.AI_POSTURE,
            feedbackMessage = "허리를 조금 더 펴주세요!"
        ),
        onToggleBottomSheet = {},
        onSelectExerciseType = {},
    )
}


