package com.hye.mission.ui.agent

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.hye.shared.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.icon.AppIcon
import com.hye.shared.ui.icon.Render
import com.hye.shared.ui.icon.StyledIconBox

@Composable
fun AgentAvatar() {
    StyledIconBox (
        boxSize = AppTheme.dimens.bigDimen,
        boxBrush = Brush.linearGradient(
            colors = listOf(
                AppTheme.colors.supportBoxColor,
                AppTheme.colors.supportBoxSecondColor
            )
        )
    ){
        AppIcon.Resource(R.drawable.agen).Render(
            tint = AppTheme.colors.background,
            modifier = Modifier.padding(AppTheme.dimens.xs)
        )
    }
}