package com.hye.mission.ui.components.recording.simulation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hye.shared.ui.icon.StyledIconBox
import com.hye.shared.theme.AppTheme

@Preview
@Composable
fun CameraSimulationView() {
    StyledIconBox(
        modifier = Modifier
            .fillMaxSize(),
        boxColor = AppTheme.colors.darkScreen
    ) {
        Icon(
            imageVector = Icons.Default.VideocamOff,
            contentDescription = null,
            tint = AppTheme.colors.background.copy(AppTheme.dimens.alphaMuted),
            modifier = Modifier.size(AppTheme.dimens.bigDimen)
        )
    }
}