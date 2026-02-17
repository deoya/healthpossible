package com.hye.shared.ui.button

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.hye.shared.ui.icon.ArrowLeftIcon
import com.hye.shared.ui.icon.SlideIcon
import com.hye.shared.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SlideButton(
    modifier: Modifier = Modifier,
    // 1. 데이터 (Composable Content)
    content: @Composable () -> Unit,
    onSlideComplete: () -> Unit = {},
    // 2. 스타일 설정
    containerHeight: Dp = AppTheme.dimens.bigDimen,
    containerColor: Color = AppTheme.colors.background,
    thumbSize: Dp = AppTheme.dimens.thumbSize,
    thumbColor: Color = AppTheme.colors.mainColor,
    thumbIcon: @Composable () -> Unit = { SlideIcon() },
    arrowHintColor: Color = thumbColor
) {
    val density = LocalDensity.current
    val thumbSizePx = with(density) { thumbSize.toPx() }
    val scope = rememberCoroutineScope()

    val offsetX = remember { Animatable(0f) }
    var trackWidthPx by remember { mutableFloatStateOf(0f) }

    // 드래그 로직
    val draggableState = rememberDraggableState { delta ->
        val maxDragDistance = trackWidthPx - thumbSizePx
        val newOffset = offsetX.value + delta
        scope.launch {
            offsetX.snapTo(newOffset.coerceIn(-maxDragDistance, 0f))
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        LaunchedEffect(maxWidth) {
            trackWidthPx = with(density) { maxWidth.toPx() }
        }

        // ✅ SimplePillButton을 재사용하되, 드래그 기능을 주입
        SimplePillButton(
            content = content, // 외부에서 받은 Composable 전달
            containerHeight = containerHeight,
            containerColor = containerColor,
            thumbSize = thumbSize,
            thumbColor = thumbColor,
            thumbIcon = thumbIcon,

            // A. 클릭 비활성화 (드래그와 충돌 방지)
            onClick = null,

            // B. 드래그 로직 주입
            thumbModifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        val maxDragDistance = trackWidthPx - thumbSizePx
                        val threshold = -(maxDragDistance * 0.7f) // 70% 이상 밀어야 성공

                        if (offsetX.value <= threshold) {
                            onSlideComplete()
                            scope.launch {
                                delay(300)
                                offsetX.animateTo(0f, animationSpec = tween(500))
                            }
                        } else {
                            scope.launch {
                                offsetX.animateTo(0f, animationSpec = tween(500))
                            }
                        }
                    }
                ),

            // C. 배경 화살표 힌트 추가
            backgroundContent = {
                Row(
                    modifier = Modifier
                        .padding(end = thumbSize + AppTheme.dimens.xxxs)
                        .alpha(AppTheme.dimens.alphaMuted),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArrowLeftIcon(color = arrowHintColor)
                }
            }
        )
    }
}

// [편의용 오버로딩] String 텍스트를 받는 SlideButton
@Composable
fun SlideButton(
    modifier: Modifier = Modifier,
    text: String,
    subText: String? = null,
    textColor: Color = AppTheme.colors.textPrimary,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    subTextColor: Color = AppTheme.colors.textSecondary,
    subTextStyle: TextStyle = MaterialTheme.typography.labelSmall,
    onSlideComplete: () -> Unit = {},
    containerHeight: Dp = AppTheme.dimens.bigDimen,
    containerColor: Color = AppTheme.colors.background,
    thumbSize: Dp = AppTheme.dimens.thumbSize,
    thumbColor: Color = AppTheme.colors.mainColor,
    thumbIcon: @Composable () -> Unit = { SlideIcon() },
    arrowHintColor: Color = thumbColor
) {
    SlideButton(
        modifier = modifier,
        onSlideComplete = onSlideComplete,
        containerHeight = containerHeight,
        containerColor = containerColor,
        thumbSize = thumbSize,
        thumbColor = thumbColor,
        thumbIcon = thumbIcon,
        arrowHintColor = arrowHintColor,
        content = {
            Column(verticalArrangement = Arrangement.Center) {
                if (!subText.isNullOrEmpty()) {
                    Text(subText, style = subTextStyle, color = subTextColor)
                }
                Text(text, style = textStyle, fontWeight = FontWeight.Bold, color = textColor)
            }
        }
    )
}