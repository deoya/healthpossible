package com.hye.shared.components.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hye.shared.theme.AppTheme

@Composable
fun StyledAlert(
    content: @Composable () -> Unit,
    needIcon: Boolean = true,
    color : Color = AppTheme.colors.backgroundMuted
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(AppTheme.dimens.mediumSmall),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(AppTheme.dimens.large),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall)
        ) {
            if(needIcon) Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = AppTheme.colors.textSecondary,
                    modifier = Modifier
                        .size(AppTheme.dimens.large)
                        .padding(top = AppTheme.dimens.micro)
                )

            content()
        }
    }
}