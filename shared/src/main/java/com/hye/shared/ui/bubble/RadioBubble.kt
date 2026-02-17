package com.hye.shared.ui.bubble

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.alphaPrimary
import com.hye.shared.theme.toPx
import com.hye.shared.theme.toSp
import com.hye.shared.ui.text.TextBody
import kotlinx.coroutines.delay

@Composable
fun RadioBubble(
    message: String,
    color: Color = AppTheme.colors.mainColor,
    backgroundColor: Color = AppTheme.colors.background.alphaPrimary,
    fontHeight : TextUnit = AppTheme.dimens.xl.toSp,
    modifier: Modifier = Modifier,
    isTyping: Boolean = false
) {
    val strokeWidth =  AppTheme.dimens.agentBubbleStrokeWidth.toPx// 테두리 두께
    val gap = AppTheme.dimens.agentBubbleStrokeGap.toPx // 이중 테두리 사이 간격

// 육각형 HUD 스타일 버블
    Box(
        modifier = modifier
            .padding(AppTheme.dimens.xxs) // 그림자나 외곽선 여유 공간
            .drawBehind {
                // 육각형 경로 생성 함수
                fun createHexagonPath(inset: Float): Path {
                    val path = Path()
                    val w = size.width
                    val h = size.height

                    val left = inset
                    val top = inset
                    val right = w - inset
                    val bottom = h - inset

                    val cutSize = h * 0.2f
                    // 시작점: 왼쪽 위 (깎인 부분 시작)
                    path.moveTo(left, top + cutSize)
                    // 왼쪽 아래
                    path.lineTo(left, bottom - cutSize)
                    // 아래쪽 왼쪽 (깎인 부분)
                    path.lineTo(left + cutSize * 0.6f, bottom)
                    // 아래쪽 오른쪽
                    path.lineTo(right - cutSize * 0.6f, bottom)
                    // 오른쪽 아래
                    path.lineTo(right, bottom - cutSize)
                    // 오른쪽 위
                    path.lineTo(right, top + cutSize)
                    // 위쪽 오른쪽
                    path.lineTo(right - cutSize * 0.6f, top)
                    // 위쪽 왼쪽 (원점 복귀)
                    path.lineTo(left + cutSize * 0.6f, top)

                    path.close()
                    return path
                }

                // 1. 바깥쪽 테두리 경로
                val outerPath = createHexagonPath(0f)

                // 2. 안쪽 테두리 경로 (간격만큼 들어감)
                val innerPath = createHexagonPath(gap + strokeWidth)

                drawPath(
                    path = outerPath,
                    color = backgroundColor,
                    style = Fill
                )

                // 바깥쪽 테두리 그리기
                drawPath(
                    path = outerPath,
                    color = color,
                    style = Stroke(width = strokeWidth)
                )

                // 안쪽 테두리 그리기 (이중선 효과)
                drawPath(
                    path = innerPath,
                    color = color.copy(alpha = 0.6f), // 안쪽은 살짝 연하게
                    style = Stroke(width = strokeWidth)
                )
            }
            .padding(horizontal = AppTheme.dimens.xxxxl, vertical = AppTheme.dimens.l) // 텍스트 내부 여백
    ) {
        if (isTyping) {
            TypingIndicator(color)
        } else {
            TextBody(
                text = message,
                fontWeight = FontWeight.Bold,
                lineHeight = fontHeight
            )
        }
    }
}

// --- Typing Animation Component ---
@Composable
fun TypingIndicator(color:Color) {
    val dots = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    dots.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 600
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.width(AppTheme.dimens.xxxxxxl)
    ) {
        dots.forEach { animatable ->
            Box(
                modifier = Modifier
                    .padding(horizontal = AppTheme.dimens.xxxxxs)
                    .size(AppTheme.dimens.xxxs)
                    .alpha(animatable.value)
                    .background(color, CircleShape)
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, backgroundColor = 0xFF888888) // 배경이 어두울 때 투명도 확인 용이
@Composable
fun PreviewRadioMessage() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Case 1: Short Message
                RadioBubble(message = "어쩌고 저쩌고\n재잘재잘")

                Spacer(modifier = Modifier.height(30.dp))

                // Case 2: Long Message
                RadioBubble(message = "사용자의 움직임이 감지되었습니다.\n운동을 시작할까요?")

                Spacer(modifier = Modifier.height(30.dp))

                // Case 3: Typing State
                RadioBubble(message = "", isTyping = true)
            }
        }
    }
}

fun Modifier.alpha(alpha: Float) = this.then(Modifier.graphicsLayer(alpha = alpha))