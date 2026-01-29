package com.hye.healthpossible.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hye.shared.animation.Direction
import com.hye.shared.animation.slideFadeInAnimation


@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit = {}
) {

    var (offsetX, offsetY, alpha) =
        slideFadeInAnimation(
            direction = Direction.Bottom,
            distance = 60f
        )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(0.7f)
                .graphicsLayer {
                    alpha = alpha
                    translationY = offsetY
                }
        ) {
            Text("체크")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen()
}