package com.hye.healthpossible.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.hye.healthpossible.R
import com.hye.healthpossible.ui.component.SplashContent
import com.hye.shared.ui.animation.DelayInfo
import com.hye.shared.ui.animation.Direction
import com.hye.shared.ui.animation.lowSpringAnimation
import com.hye.shared.ui.animation.slideFadeInAnimation
import com.hye.shared.ui.text.LabelSmall
import com.hye.shared.ui.common.light
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.ScaffoldContentPaddingWithBottomBar
import com.hye.shared.util.text


@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit = {}
) {
    val logoDuration = AppTheme.dimens.logoDuration    // 로고 애니메이션 시간
    val gapTime = AppTheme.dimens.gapTime     // 로고 끝나고 타이틀 나올 때까지 대기 시간
    val titleDelay = (logoDuration - gapTime).toLong() // 타이틀 시작 시간 계산
    val titleDuration = AppTheme.dimens.titleDuration //타이틀 애니메이션 시간
    val delayed = AppTheme.dimens.delayed

    val (logoScale, logoAlpha) = lowSpringAnimation(durationMillis = logoDuration)
    val (titleOffsetX, titleOffsetY, titleAlpha) =
        slideFadeInAnimation(
            direction = Direction.Bottom,
            distance = AppTheme.dimens.splashLogoDistance,
            delayInfo = DelayInfo(startDelay = titleDelay),
            durationMillis =titleDuration
        )

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(titleDelay + titleDuration + delayed)
        onSplashFinished()
    }

    SplashContent(
        logo = {Box(
            modifier = Modifier
                .size( AppTheme.dimens.splashLogoSize)
                .padding(bottom = AppTheme.dimens.xxl)
                .scale(logoScale)
                .alpha(logoAlpha)
        ) {
            AsyncImage(
                R.drawable.logo_ci,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }},
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimens.splashTitlePadding)
                    .graphicsLayer {
                        alpha = titleAlpha
                        translationY = titleOffsetY
                    }
            ) {
                AsyncImage(
                    R.drawable.title,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        copyright = {
            LabelSmall(
                text = R.string.copyright.text,
                color = AppTheme.colors.textPrimary.light,
                modifier = Modifier
                    .padding(AppTheme.dimens.ScaffoldContentPaddingWithBottomBar)
                    .alpha(titleAlpha)
            )
        }

    )
}


@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen()
}