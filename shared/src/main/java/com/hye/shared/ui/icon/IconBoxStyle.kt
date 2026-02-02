package com.hye.shared.ui.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme


@Composable
fun StyledIconBox(
    modifier: Modifier = Modifier,
    boxSize : Dp? = AppTheme.dimens.iconBox,
    shape : Dp = AppTheme.dimens.md,
    boxColor : Color = AppTheme.colors.backgroundMuted,
    icon: @Composable () -> Unit
){
    val sizeModifier = if (boxSize != null) Modifier.size(boxSize) else Modifier

    Box(
        modifier = modifier
            .then(sizeModifier)
            .clip(RoundedCornerShape(shape))
            .background(boxColor),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}