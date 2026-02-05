package com.hye.mission.ui.components.recording.exercise.posture

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.AiSessionMode
import com.hye.mission.ui.state.RecordState
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.StyledDivider
import com.hye.shared.ui.sheet.CommonBottomSheet
import com.hye.shared.ui.text.TextDescription
import com.hye.shared.ui.text.TitleMedium
import com.hye.features.mission.R
import com.hye.shared.util.text


@Composable
fun BoxScope.AiPostureBottomSheet(
    state: RecordState,
    progressText: String,
    onToggleBottomSheet: (Boolean) -> Unit,
    onSelectExercise: (AiExerciseType) -> Unit
) {
    // B. 바텀시트 (운동 종류 변경)
    AnimatedVisibility(
        visible = state.isBottomSheetOpen,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(600)
        ),
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        CommonBottomSheet(
            sheetHeightFraction = AppTheme.dimens.sheetHeight,
            onDismissRequest = { onToggleBottomSheet(false) },
            header = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = AppTheme.dimens.xxl,
                                vertical = AppTheme.dimens.xxs
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            TitleMedium(R.string.mission_change_exercise_type.text)
                            TextDescription(progressText)
                        }
                    }
                    StyledDivider()
                }
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = AppTheme.dimens.xxl,
                    vertical = AppTheme.dimens.xxs
                ),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.md),
                modifier = Modifier.padding(bottom = AppTheme.dimens.bottomBarPadding)
            ) {
                // AiRepMode(종목 선택 모드)일 때만 리스트 표시
                if (state.sessionMode is AiSessionMode.AiRepMode) {
                    items(AiExerciseType.values()) { type ->
                        // 현재 선택된 운동인지 확인
                        val currentType =
                            (state.sessionMode as AiSessionMode.AiRepMode).exerciseType
                        val isSelected = currentType == type

                        ExerciseTypeSelectorItem(
                            type = currentType,
                            isSelected = isSelected,
                            onClick = { onSelectExercise(type) }
                        )
                    }
                }
            }
        }
    }
}