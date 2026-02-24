package com.hye.shared.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LowSpringAnimation(durationMillis:Int = 800, dampingRatio: Float = 0.6f, delayInfo:DelayInfo = DelayInfo()): Pair<Float, Float> {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(delayInfo.startDelay)
        launch {
            alpha.animateTo(
                1f,
                tween(durationMillis)
            )
        }
        launch {
            scale.animateTo(
                1f,
                spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
            )
        }
        delay(delayInfo.endDelay)
    }

    return Pair(scale.value, alpha.value)
}