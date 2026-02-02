package com.hye.shared.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hye.shared.theme.AppTheme


@Composable
fun StyledDivider(
    color : Color = AppTheme.colors.divider,
    thickness : Dp = 1.dp,
    padding : Dp = AppTheme.dimens.md,
    modifier : Modifier = Modifier
) = Divider(
    color = AppTheme.colors.divider,
    thickness = 1.dp,
    modifier = modifier.padding(vertical = AppTheme.dimens.md)
)