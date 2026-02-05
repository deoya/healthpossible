package com.hye.mission.ui.components.ondevice.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.hye.features.mission.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.text
import com.hye.shared.R as commonR

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDeniedContent(
    status: PermissionStatus.Denied,
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.md)
    ) {
        val textToShow = if (status.shouldShowRationale) {
            R.string.mission_camera_permission_for_exercise.text
        } else {
            commonR.string.camera_permission_denied.text
        }

        Text(
            text = textToShow,
            color = AppTheme.colors.background,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.xxxxl)
        )
        Button(onClick = onRequestPermission) {
            Text(commonR.string.request_permission.text)
        }
    }
}