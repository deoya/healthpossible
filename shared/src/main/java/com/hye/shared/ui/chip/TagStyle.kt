package com.hye.shared.ui.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.hye.shared.ui.text.LabelMedium
import com.hye.shared.theme.AppTheme


@Composable
fun Tag(name: String, color: Color = AppTheme.colors.mainColor) =
    LabelMedium(text = name, color = color, fontWeight = FontWeight.Bold)

// content를 넣는 순서에 따라 icon을 앞 뒤로 자유롭게 배치 가능
@Composable
fun StyledTag(
    onClick: (String) -> Unit = {},
    color: Color = AppTheme.colors.mainColorLight,
    border: BorderStroke? = null,
    shape: Dp = AppTheme.dimens.xxs,

    horizontal : Dp = AppTheme.dimens.xs,
    vertical : Dp = AppTheme.dimens.xxxs,

    content: @Composable () -> Unit = {},
    content2: @Composable () -> Unit = {},

) {
    Surface(
        shape = RoundedCornerShape(shape),
        color = color,
        border = border,
        modifier = Modifier.clickable { onClick }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = horizontal,
                vertical = vertical
            ),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxs)
        ) {
            content()
            content2()
        }
    }
}
