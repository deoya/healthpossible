package com.hye.shared.ui.bubble

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp

@Composable
fun LoadingMessageBubble(text: String, color: Color = Color.White) {
    // 1. 인라인 콘텐츠 식별자
    val inlineId = "typingIndicator"

    // 2. 텍스트와 '가상의 글자 자리'를 이어붙인 문자열 생성
    val annotatedText = buildAnnotatedString {
        append(text)
        appendInlineContent(inlineId, "[...]")
    }
    // 3. 선언한 자리에 실제로 들어갈 컴포저블(TypingIndicator) 정의
    val inlineContent = mapOf(
        inlineId to InlineTextContent(
            Placeholder(
                width = 3.em,
                height = 1.em,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TypingIndicator(color = color)
            }
        }
    )

    Text(
        text = annotatedText,
        inlineContent = inlineContent,
        modifier = Modifier.padding(AppTheme.dimens.l),
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
            lineHeight = AppTheme.dimens.xxl.toSp,
            textAlign = TextAlign.Center
        ),
        color = color
    )
}


// --- Typing Animation Component ---
@Composable
fun TypingIndicator(color:Color) {
    // 1. 무한 반복 애니메이션을 관리하는 전용 트랜지션 객체 생성
    val infiniteTransition = rememberInfiniteTransition(label = "typing")

    // 2. 각 점마다 애니메이션 상태를 생성하되, 시작 지점(initialStartOffset)을 다르게 주어 파도타기 효과를 만듭니다.
    val dots = List(3) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1200
                    0.0f at 0 with FastOutSlowInEasing
                    1.0f at 300 with FastOutSlowInEasing
                    0.0f at 600 with FastOutSlowInEasing
                    0.0f at 1200 // 나머지 시간은 멈춰 대기
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = androidx.compose.animation.core.StartOffset(index * 200)
            ),
            label = "dotAlpha_$index"
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.width(AppTheme.dimens.xxxxxxl)
    ) {
        dots.forEach { alphaState ->
            Box(
                modifier = Modifier
                    .padding(horizontal = AppTheme.dimens.xxxxxs)
                    .size(AppTheme.dimens.xxxs)
                    .alpha(alphaState.value)
                    .background(color, CircleShape)
            )
        }
    }
}
