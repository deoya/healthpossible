package com.hye.mission.ui.components.recording.exercise.posture

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.hye.domain.model.mission.types.ExerciseAgentType
import com.hye.features.mission.R
import com.hye.mission.ui.state.RecordState
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.button.SlideButton
import com.hye.shared.ui.text.FeedbackBubble
import com.hye.shared.util.text

@Preview
@Composable
fun AiPostureContent_Preview() {
    AiPostureContent(
        state = RecordState(ExerciseAgentType.AI_POSTURE),
        onToggleBottomSheet = {},
    )
}

@Composable
fun AiPostureContent(
    state: RecordState,
    onToggleBottomSheet: (Boolean) -> Unit,
) {
    // 1. 현재 상태에 따른 텍스트 (RecordState의 계산 프로퍼티 사용)
    val progressText = state.progressLabel
    val subText = "${state.currentExerciseLabel} " + R.string.mission_in_progress.text
    AnimatedVisibility(
        visible = state.feedbackMessage.isNotEmpty(),
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        FeedbackBubble(message = state.feedbackMessage)
    }
    SlideButton(
        modifier = Modifier.shadow(AppTheme.dimens.s),
        text = progressText,
        subText = subText,
        onSlideComplete = { onToggleBottomSheet(true) }, // 슬라이드 완료 시 바텀시트 열기
        thumbColor = AppTheme.colors.supportAgentColor,
        thumbIcon = {
            Icon(
                painter = painterResource(R.drawable.exercise_icon),
                tint = AppTheme.colors.background,
                contentDescription = null,
                modifier = Modifier
                    .size(AppTheme.dimens.xxxxxxl)
                    .graphicsLayer { scaleX = -1f }
            )
        }
    )
}
