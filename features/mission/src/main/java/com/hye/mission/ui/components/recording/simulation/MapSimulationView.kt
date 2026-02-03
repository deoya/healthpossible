package com.hye.mission.ui.components.recording.simulation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.hye.shared.theme.AppTheme

// 지도 색상은 상수로 관리하거나 테마에 추가해도 좋습니다.
private val RunningOrange = Color(0xFFF97316)

@Composable
fun MapSimulationView() {
    val gridLineColor = AppTheme.colors.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.mapSimulationColor)
    ) {
        // Map Grid Lines & Path Simulation
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSize = 100f
            val width = size.width
            val height = size.height

            // Draw Grid
            for (i in 0..width.toInt() step gridSize.toInt()) {
                drawLine(gridLineColor, start = Offset(i.toFloat(), 0f), end = Offset(i.toFloat(), height), strokeWidth = 2f)
            }
            for (i in 0..height.toInt() step gridSize.toInt()) {
                drawLine(gridLineColor, start = Offset(0f, i.toFloat()), end = Offset(width, i.toFloat()), strokeWidth = 2f)
            }

            // Draw User Path (Current Location)
            drawCircle(RunningOrange, radius = 20f, center = center)
            drawCircle(RunningOrange.copy(alpha = 0.3f), radius = 60f, center = center)
        }
    }
}