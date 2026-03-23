package com.hye.shared.ui.bubble

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.alphaPrimary
import com.hye.shared.theme.toPx
import com.hye.shared.theme.toSp
import com.hye.shared.ui.text.TextBody

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
// --- Preview ---
@Preview(showBackground = true, backgroundColor = 0xFF888888)
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
