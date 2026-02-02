package com.hye.mission.ui.components.recording.exercise.posture

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.ExerciseAgentType
import com.hye.mission.ui.components.recording.layout.BaseSessionLayout
import com.hye.mission.ui.components.recording.simulation.CameraSimulationView
import com.hye.mission.ui.model.RecordState
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.text.FeedbackBubble

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
            Column(
                modifier = Modifier.padding(AppTheme.dimens.xxl),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.md)
            ) {
                FeedbackBubble(message = state.feedbackMessage)

                AiPostureCard(
                    state = state,
                    onToggleBottomSheet = onToggleBottomSheet,
                    onSelectExercise = onSelectExerciseType,
                )

            }
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


