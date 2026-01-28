package com.hye.shared.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
    val xxxxl :Dp = 36.dp,
    val xxxxxl: Dp = 40.dp,

    val progressSize:Dp = 88.dp,
    val bigBtn: Dp = 50.dp,
    val iconBox: Dp =52.dp,
    val bottomBarPadding : Dp = 80.dp,
    val dropDownMenuAddMisionPadding :Dp = 110.dp,

    val alphaMuted: Float = 0.2f,
    val alphaPrimary: Float = 0.8f
)


val Dp.toSp
    @Composable
    get() =  with(LocalDensity.current) { this@toSp.toSp() }
/**
 * LazyColumn과 같은 스크롤 컨테이너를 위한 콘텐츠 패딩으로,
 * 하단에 추가적인 공간(보통 BottomAppBar를 위한 공간)을 확보합니다.
 */
val HPAppDimens.ScaffoldContentPaddingWithBottomBar: PaddingValues
    @Composable
    @ReadOnlyComposable
    get() {
        val layoutDirection = LocalLayoutDirection.current
        val basePadding = PaddingValues(md)
        val bottomBarHeight =60.dp// 하단 여백 높이 (중앙 관리)

        return PaddingValues(
            start = basePadding.calculateStartPadding(layoutDirection),
            end = basePadding.calculateEndPadding(layoutDirection),
            top = md,
            bottom = md + bottomBarHeight
        )
    }


val LocalDimens = staticCompositionLocalOf {
    HPAppDimens()
}
