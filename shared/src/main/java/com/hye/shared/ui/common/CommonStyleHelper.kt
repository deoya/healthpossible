package com.hye.shared.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme

enum class Orientation {
    Column, Row
}

data class IconStyle(
    val image : ImageVector? = null,
    val contentDescription : String? = null,
    val tint : Color? = null,
    val size : Dp? = null,
)

val Color.light : Color
    @ReadOnlyComposable
    @Composable
    get() = this.copy(alpha = AppTheme.dimens.alphaMuted)