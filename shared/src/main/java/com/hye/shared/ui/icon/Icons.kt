package com.hye.shared.ui.icon

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.SwipeLeftAlt
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.common.light

@Composable
fun CloseIcon(
    image : ImageVector = Icons.Outlined.Close,
    color : Color =  AppTheme.colors.mainColor,
    size : Dp = AppTheme.dimens.sm
) = Icon(
        imageVector = image,
        "Close",
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
    contentDescription = "Calendar",
    tint = color,
    modifier = Modifier.size(size)
)

@Composable
fun ArrowLeftIcon(
    image : ImageVector = Icons.Filled.KeyboardDoubleArrowLeft,
    color : Color =  AppTheme.colors.mainColor.light,
    size : Dp = AppTheme.dimens.xxxxl
) = Icon(
    image,
    contentDescription = "ArrowLeft Hint Icon",
    tint = color,
    modifier = Modifier.size(size)
)

@Composable
fun SlideIcon(
    image : ImageVector = Icons.Filled.SwipeLeftAlt,
    color : Color =  AppTheme.colors.background,
    size : Dp = AppTheme.dimens.xxxl,
) = Icon(
    image,
    contentDescription = "Slide Action",
    tint = color,
    modifier = Modifier.size(size)
)

@Composable
fun SimplePillIcon(
    image : ImageVector = Icons.Filled.TouchApp,
    color : Color =  AppTheme.colors.background,
    size : Dp = AppTheme.dimens.xxxl,
) = Icon(
    image,
    contentDescription = "Touch Action",
    tint = color,
    modifier = Modifier.size(size)
)

