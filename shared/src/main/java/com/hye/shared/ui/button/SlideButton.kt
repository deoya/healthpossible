package com.hye.shared.ui.button

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    // 1. 데이터
    text: String = "",
    textColor: Color = AppTheme.colors.textPrimary,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    subText: String? = "",
    subTextColor: Color = AppTheme.colors.textSecondary,
    subTextStyle: TextStyle = MaterialTheme.typography.labelSmall,
    onSlideComplete: () -> Unit = {},
    // 2. 슬라이더 설정 (기본값 제공)
    containerHeight: Dp = AppTheme.dimens.bigDimen,
    containerColor: Color = AppTheme.colors.background,
    // 3. thumb 스타일
    thumbSize: Dp = AppTheme.dimens.thumbSize,
    thumbColor: Color = AppTheme.colors.mainColor,
    thumbIcon: @Composable ()-> Unit = { SlideIcon() },
    // 4. 힌트 아이콘 스타일
    arrowHintColor: Color = thumbColor
){
    SlideButton(
        modifier = modifier,
        onSlideComplete = onSlideComplete,
        containerHeight = containerHeight,
        thumbSize = thumbSize,
        containerColor = containerColor,
        thumbColor = thumbColor,
        arrowHintColor = arrowHintColor,
        thumbIcon = thumbIcon,
        // 🚀 여기가 핵심: String을 받아서 Composable(Text)로 변환하여 넘김
        content = {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                if (!subText.isNullOrEmpty()) {
                    Text(subText, style = subTextStyle, color = subTextColor)
                }
                Text(text, style = textStyle, fontWeight = FontWeight.Bold, color = textColor)
            }
        }
    )
}

@Preview(showBackground= true, backgroundColor = 0xFFF0EAE2)
@Composable
fun SlideButton(
    modifier: Modifier = Modifier,
    // 1. 데이터
    content: @Composable () -> Unit = {},
    onSlideComplete: () -> Unit = {},
    // 2. 슬라이더 설정 (기본값 제공)
    containerHeight: Dp = AppTheme.dimens.bigDimen,
    containerColor: Color = AppTheme.colors.background,
    // 3. thumb 스타일
    thumbSize: Dp = AppTheme.dimens.thumbSize,
    thumbColor: Color = AppTheme.colors.mainColor,
    thumbIcon: @Composable ()-> Unit = { SlideIcon() },
    // 4. 힌트 아이콘 스타일
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
        // 오른쪽(0)에서 왼쪽(-max)으로만 이동 가능하게 제한
        scope.launch {
            offsetX.snapTo(newOffset.coerceIn(-maxDragDistance, 0f))
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .height(containerHeight)
            .fillMaxWidth()
            .clip(RoundedCornerShape(containerHeight / 2)) // 높이의 절반으로 둥글게
            .background(containerColor)
            .padding(AppTheme.dimens.xxs)
    ) {
        LaunchedEffect(maxWidth) {
            trackWidthPx = with(density) { maxWidth.toPx() }
        }

        // 1. 텍스트 정보 (왼쪽)
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = AppTheme.dimens.xxl, end = thumbSize + AppTheme.dimens.md), // 텍스트가 버튼 겹치지 않게 패딩
            verticalArrangement = Arrangement.Center
        ) {
           content()
        }

        // 2. 배경 화살표 힌트 (오른쪽 뒤)
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = thumbSize + AppTheme.dimens.xxxs) // 썸 뒤쪽에 위치
                .alpha(AppTheme.dimens.alphaMuted),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArrowLeftIcon(color = arrowHintColor)
        }

        // 3. 드래그 핸들 (Thumb)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .size(thumbSize)
                .clip(CircleShape)
                .background(thumbColor)
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        val maxDragDistance = trackWidthPx - thumbSizePx
                        val threshold = -(maxDragDistance * 0.7f)

                        if (offsetX.value <= threshold) {
                            // 성공 시
                            onSlideComplete()
                            // 시각적 피드백 후 복귀
                            scope.launch {
                                delay(300)
                                offsetX.animateTo(0f, animationSpec = tween(500))
                            }
                        } else {
                            // 실패 시 제자리로 튕겨 돌아감 (Spring 효과)
                            scope.launch {
                                offsetX.animateTo(0f, animationSpec = tween(500))
                            }
                        }
                    }
                )
        ) {
            thumbIcon()
        }
    }
}
