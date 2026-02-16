package com.hye.shared.ui.text
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HeightFittingText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    weight: FontWeight = FontWeight.Normal,
    contentAlignment: Alignment = Alignment.Center
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        val density = LocalDensity.current

        // 1. 컨테이너의 높이를 기준으로 초기 폰트 크기 계산 (꽉 채우기 위해)
        // * 0.7f는 텍스트의 상하 여백(Ascent/Descent)을 고려한 안전 계수
        val initialFontSize = with(density) { (maxHeight * 0.7f).toSp() }

        var fontSize by remember(text, maxHeight) { mutableStateOf(initialFontSize) }
        var readyToDraw by remember(text, maxHeight) { mutableStateOf(false) }

        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            fontWeight = weight,
            onTextLayout = { result ->
                // 2. 만약 너비가 박스를 넘어가거나 높이가 넘친다면 사이즈를 줄임
                if (result.didOverflowWidth || result.didOverflowHeight) {
                    fontSize *= 0.9f // 10%씩 줄임
                } else {
                    // 딱 맞거나 작으면 그리기 시작
                    readyToDraw = true
                }
            },
            modifier = Modifier.drawWithContent {
                if (readyToDraw) drawContent()
            }
        )
    }
}