package com.hye.shared.ui.icon

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

sealed interface AppIcon {
    data class Vector(val value: ImageVector) : AppIcon
    data class Resource(@DrawableRes val resId: Int) : AppIcon
}

@Composable
fun AppIcon.Render(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    when (this) {
        is AppIcon.Vector -> Icon(
            imageVector = value,
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
        is AppIcon.Resource -> Icon(
            painter = painterResource(resId),
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
    }
}
