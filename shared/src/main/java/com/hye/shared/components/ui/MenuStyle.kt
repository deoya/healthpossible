package com.hye.shared.components.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme

data class MenuStyle (
    val modifier : Modifier? = null,
    val icon : ImageVector,
    val iconTint : Color,
    val iconSize : Dp? = null,
    val contentDescription : String? = null,
)

@Composable
fun StyledIconMenu(
    style: MenuStyle,
    content : @Composable () -> Unit,
){
    Column(
        modifier = style?.modifier ?: Modifier
            .padding(vertical = AppTheme.dimens.extraLarge, horizontal =AppTheme.dimens.tiny)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
    ) {
        Icon(
            imageVector = style.icon,
            contentDescription = style.contentDescription,
            tint = style.iconTint,
            modifier = Modifier.size(style.iconSize?: AppTheme.dimens.huge)
        )
        content()
    }
}