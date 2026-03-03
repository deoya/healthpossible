package com.hye.shared.ui.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme


@Composable
fun StyledIconBox(
    modifier: Modifier = Modifier,
    boxSize: Dp? = AppTheme.dimens.iconBox,
    cornerRadius: Dp = AppTheme.dimens.md,
    boxColor: Color? = null,
    boxBrush: Brush? = null,
    icon: @Composable () -> Unit
) {
    val sizeModifier = boxSize?.let { Modifier.size(it) } ?: Modifier

    val finalBrush = when {
        boxBrush != null -> boxBrush
        boxColor != null -> SolidColor(boxColor)
        else -> SolidColor(AppTheme.colors.backgroundMuted)
    }

    Box(
        modifier = modifier
            .then(sizeModifier)
            .clip(RoundedCornerShape(cornerRadius))
            .background(finalBrush),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}