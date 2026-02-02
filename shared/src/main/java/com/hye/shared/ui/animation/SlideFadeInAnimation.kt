package com.hye.shared.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

enum class Direction{ Top, Bottom, Start, End }

data class StartOffset(
    val x: Float,
    val y: Float
)

fun Direction.toStartOffset(distance: Float): StartOffset =
    when (this) {
        Direction.Top    -> StartOffset(0f, -distance)
        Direction.Bottom -> StartOffset(0f, distance)
        Direction.Start  -> StartOffset(-distance, 0f)
        Direction.End    -> StartOffset(distance, 0f)
    }

@Composable
fun slideFadeInAnimation (
    direction: Direction,
    distance: Float = 50f,
    durationMillis: Int = 800,
    delayInfo:DelayInfo = DelayInfo()
): Triple<Float, Float, Float> {

    val startOffset = direction.toStartOffset(distance)

    val offsetX = remember { Animatable(startOffset.x) }
    val offsetY = remember { Animatable(startOffset.y) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(delayInfo.startDelay)
        launch {
            offsetX.animateTo(0f, tween(durationMillis))
        }
        launch {
            offsetY.animateTo(0f, tween(durationMillis))
        }
        launch {
            alpha.animateTo(1f, tween(durationMillis))
        }
        delay(delayInfo.endDelay)
    }

    return Triple(offsetX.value, offsetY.value, alpha.value)
}