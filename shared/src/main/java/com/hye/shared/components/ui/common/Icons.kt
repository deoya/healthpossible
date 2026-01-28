package com.hye.shared.components.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hye.shared.theme.AppTheme

@Composable
fun CloseIcon(
    image : ImageVector = Icons.Outlined.Close,
    color : Color =  AppTheme.colors.mainColor,
    size : Dp = AppTheme.dimens.sm
) = Icon(
        imageVector = image,
        null,
        modifier = Modifier.size(size),
        tint =color
)

@Composable
fun CalendarIcon(
    image : ImageVector = Icons.Default.CalendarToday,
    color : Color =  AppTheme.colors.textSecondary.light,
    size : Dp = AppTheme.dimens.sm
) = Icon(
    image,
    contentDescription = null,
    tint = color,
    modifier = Modifier.size(size)
)