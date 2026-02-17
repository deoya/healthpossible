package com.hye.shared.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class HPAppDimens(
    val zero : Dp = 0.dp,
    val one : Dp = 1.dp,
    val xxxxxs: Dp = 2.dp,
    val xxxxs: Dp = 4.dp,
    val xxxs: Dp = 6.dp,
    val xxs: Dp = 8.dp,
    val xs: Dp = 10.dp,
    val s: Dp = 12.dp,
    val sm: Dp = 14.dp,
    val md: Dp = 16.dp,
    val m: Dp = 18.dp,
    val l: Dp = 20.dp,
    val xl: Dp = 22.dp,
    val xxl: Dp = 24.dp,
    val xxxl: Dp = 28.dp,
    val xxxxl: Dp = 32.dp,
    val xxxxxl :Dp = 36.dp,
    val xxxxxxl: Dp = 40.dp,

    val circularProgressSize:Dp = 88.dp,
    val RunningCircularProgressSize:Dp = 100.dp,
    val progressDurationMillis:Int = 1000,
    val bigBtn: Dp = 50.dp,
    val iconBox: Dp =52.dp,
    val bigDimen: Dp = 80.dp,
    val runningSimplePillHeight : Dp = 120.dp,
    val dropDownMenuAddMisionPadding :Dp = 110.dp,

    val alphaMuted: Float = 0.2f,
    val alphaPrimary: Float = 0.8f,

    val executionContentHeight : Dp = 60.dp,
    val executionContentLabelHeight : Float = 0.3f,
    val executionContentInfoHeight : Float = 0.7f,
    val sheetHeight: Float = 0.6f,
    //splash관련
    val logoDuration:Int = 1200,
    val splashLogoDistance:Float = 60f,
    val gapTime : Int = 300,
    val titleDuration : Int = 800,
    val splashLogoSize : Dp = 190.dp,
    val splashTitlePadding : Dp = 100.dp,

    val delayed : Long = 2000L,

    val thumbSize : Dp = 64.dp,
    val exciseTypeThumbnailSize : Dp = 64.dp,
    //bottombar
    val bottomBarPadding : Dp = 80.dp,
    val bottomBarCurveWidth : Dp = 85.dp,
    val bottomBarCurveHeight : Dp = 35.dp,
    val bottomBarContentHeight : Dp = 60.dp,
    val bottomBarFabSize : Dp = 64.dp,
    val bottomBarFabOffset : Dp = (-10).dp,
    val bottomBaRleisurelyHeight : Dp = 90.dp,

    //말풍선
    val agentBubbleStrokeWidth: Dp = 1.5.dp,
    val agentBubbleStrokeGap: Dp = 3.dp,

    )
val Dp.toSp
    @Composable
    @ReadOnlyComposable
    get() =  with(LocalDensity.current) { this@toSp.toSp() }
val Dp.toPx
    @Composable
    @ReadOnlyComposable
    get() =  with(LocalDensity.current) { this@toPx.toPx() }

@Composable
@ReadOnlyComposable
private fun Int.toDp(density: androidx.compose.ui.unit.Density) = with(density) { this@toDp.toDp() }

val HPAppDimens.ScaffoldContentPaddingWithBottomBar: PaddingValues
    @Composable
    get() {
        val layoutDirection = LocalLayoutDirection.current
        val density = LocalDensity.current

        val basePadding = PaddingValues(md)

        val navBarHeight = WindowInsets.navigationBars.getBottom(density).toDp(density)

        val bottomBarHeight = 60.dp

        return PaddingValues(
            start = basePadding.calculateStartPadding(layoutDirection),
            end = basePadding.calculateEndPadding(layoutDirection),
            top = md,
            bottom = md + bottomBarHeight + navBarHeight
        )
    }

val LocalDimens = staticCompositionLocalOf {
    HPAppDimens()
}
