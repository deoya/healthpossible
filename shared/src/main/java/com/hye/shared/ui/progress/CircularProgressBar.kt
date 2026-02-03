package com.hye.shared.ui.progress

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.Calculator

@Preview(backgroundColor = 0xFFBDBDBD, showBackground = true)
@Composable
fun CircularProgressBar_Preview() {
    val progress = Calculator.progress(10, 5)
    CircularProgressBar(
        progress = progress,
        iconContent = {
            Text(progress.toString())
        }
    )
}


@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    progress: Float, // 0.0f ~ 1.0f
    iconContent: @Composable () -> Unit = {}, // 중앙에 들어갈 아이콘

    size: Dp = AppTheme.dimens.circularProgressSize,
    color: Color = AppTheme.colors.background,
    trackColor: Color = AppTheme.colors.incompleteColor,
    strokeWidth: Dp = AppTheme.dimens.xxs,
    backgroundAlpha: Float = AppTheme.dimens.alphaMuted // 배경 원의 투명도
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = AppTheme.dimens.progressDurationMillis, easing = LinearOutSlowInEasing),
        label = "CircularProgressAnimation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        // 1. 배경 원 (전체 흐릿한 원)
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = color.copy(alpha = backgroundAlpha),
            trackColor = trackColor,
            strokeWidth = strokeWidth,
        )

        // 2. 진행 원 (실제 데이터 표시)
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = color,
            strokeWidth = strokeWidth,
            trackColor = trackColor,
            strokeCap = StrokeCap.Round
        )

        // 3. 중앙 아이콘
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(size - strokeWidth * 2)
        ) {
            iconContent()
        }
    }
}