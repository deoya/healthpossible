package com.hye.shared.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.hye.shared.components.ui.common.IconStyle
import com.hye.shared.theme.AppTheme


@Composable
fun StyledIconBox(
    boxSize : Dp = AppTheme.dimens.iconBox,
    shape : Dp = AppTheme.dimens.md,
    boxColor : Color = AppTheme.colors.backgroundMuted,
    iconStyle: IconStyle
){
    Box(
        modifier = Modifier
            .size(boxSize)
            .clip(RoundedCornerShape(shape))
            .background(boxColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconStyle!!.image!!,
            contentDescription = iconStyle.contentDescription,
            tint = iconStyle.tint ?: AppTheme.colors.mainColor,
            modifier = Modifier.size(iconStyle.size ?: AppTheme.dimens.xxl)
        )
    }
}