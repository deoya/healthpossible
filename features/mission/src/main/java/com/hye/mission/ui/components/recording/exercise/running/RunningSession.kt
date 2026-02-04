package com.hye.mission.ui.components.recording.exercise.running

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hye.domain.model.mission.types.AiSessionMode
import com.hye.mission.ui.components.recording.layout.BaseBottomBarContentLayout
import com.hye.mission.ui.components.recording.layout.BaseSessionLayout
import com.hye.mission.ui.components.recording.simulation.MapSimulationView
import com.hye.mission.ui.state.RecordState


@Preview
@Composable
fun RunnigSession_Preview() {
    RunningSession(
        state = RecordState(
            sessionMode = AiSessionMode.DurationMode(
                targetSeconds = 900,
                currentSeconds = 200
            ),
        ),
        onToggleTimer = { },
    )
}

@Composable
fun RunningSession(
    state: RecordState,
    onToggleTimer: () -> Unit,
) {
    BaseSessionLayout(
        backgroundContent = {
            MapSimulationView()
        },
        topBarContent = {},
        bottomBarContent = {
            BaseBottomBarContentLayout(
                content = {
                    RunningContent (
                        totalTargetValue = state.totalTargetValue,
                        currentProgressValue = state.currentProgressValue,
                        isRunning = state.isRunning,
                        currentStep = state.currentStep,
                        onToggleTimer = onToggleTimer
                    )
                }
            )
        }
    )
}
