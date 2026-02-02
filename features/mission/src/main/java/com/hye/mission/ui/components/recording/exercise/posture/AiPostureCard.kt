package com.hye.mission.ui.components.recording.exercise.posture

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.AiSessionMode
import com.hye.features.mission.R
import com.hye.mission.ui.model.RecordState
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.StyledDivider
import com.hye.shared.ui.button.SlideButton
import com.hye.shared.ui.common.CommonBottomSheet
import com.hye.shared.ui.text.TextDescription
import com.hye.shared.ui.text.TitleMedium
import com.hye.shared.util.text

@Composable
fun AiPostureCard(
    state: RecordState,
    onToggleBottomSheet: (Boolean) -> Unit,
    onSelectExercise: (AiExerciseType) -> Unit
) {
    // 1. 현재 상태에 따른 텍스트 (RecordState의 계산 프로퍼티 사용)
    val progressText = state.progressLabel
    val subText = "${state.currentExerciseLabel} " + R.string.mission_in_progress.text

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // A. 하단 슬라이드 버튼
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = AppTheme.dimens.xxxxxxl,
                    start = AppTheme.dimens.xxl,
                    end = AppTheme.dimens.xxl
                )
        ) {
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
                                TitleMedium("운동 종류 변경")
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
                            val currentType = (state.sessionMode as AiSessionMode.AiRepMode).exerciseType
                            val isSelected = currentType == type

                            ExerciseTypeSelectorItem(
                                type = currentType,
                                isSelected = isSelected,
                                onClick = { onSelectExercise(type)}
                            )
                        }
                    }
                }
            }
        }
    }
}
