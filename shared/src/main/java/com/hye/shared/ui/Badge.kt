package com.hye.shared.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hye.shared.theme.AppTheme

// (참고용) AI 뱃지 컴포넌트 분리
@Composable
fun Badge(
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colors.mainColor,
    content: @Composable () -> Unit
) {
    Surface(
        color = containerColor,//AppTheme.colors.supportAgentColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(AppTheme.dimens.l)
    ) {
        Row(
            modifier = modifier.padding(horizontal = AppTheme.dimens.xs, vertical = AppTheme.dimens.xxxs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxs)
        ) {
            content()
//            Icon(
//                Icons.Default.AutoAwesome,
//                null,
//                tint = AppTheme.colors.supportAgentColor,
//                modifier = Modifier.size(12.dp)
//            )
//            Text(
//                "AI 코칭 중",
//                fontSize = 11.sp,
//                fontWeight = FontWeight.Bold,
//                color = AppTheme.colors.supportAgentColor
//            )
        }
    }
}