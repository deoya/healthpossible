package com.hye.mission.ui.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hye.shared.theme.AppTheme

@Composable
fun PrograssBar (progress : Float){
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.xxxxs),
        color = AppTheme.colors.mainColor,
        trackColor = AppTheme.colors.mainColorLight
    )
}