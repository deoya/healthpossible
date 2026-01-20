package com.hye.shared.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class HPAppDimens(
    val micro: Dp = 2.dp,
    val tiny: Dp = 4.dp,
    val extraSmall: Dp = 8.dp,
    val small: Dp = 10.dp,
    val mediumSmall: Dp = 12.dp,
    val medium: Dp = 14.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 20.dp,
    val doubleExtraLarge: Dp = 24.dp,
    val huge: Dp = 28.dp
)

/**
 * LazyColumn과 같은 스크롤 컨테이너를 위한 콘텐츠 패딩으로,
 * 하단에 추가적인 공간(보통 BottomAppBar를 위한 공간)을 확보합니다.
 */
val HPAppDimens.ScaffoldContentPaddingWithBottomBar: PaddingValues
    @Composable
    @ReadOnlyComposable
    get() {
        val layoutDirection = LocalLayoutDirection.current
        val basePadding = PaddingValues(large)
        val bottomBarHeight =60.dp// 하단 여백 높이 (중앙 관리)

        return PaddingValues(
            start = basePadding.calculateStartPadding(layoutDirection),
            end = basePadding.calculateEndPadding(layoutDirection),
            top = large,
            bottom = large + bottomBarHeight
        )
    }


val LocalDimens = staticCompositionLocalOf {
    HPAppDimens()
}

/*
* nano
micro
tiny
extraSmall
small
mediumSmall
medium
mediumLarge
large
extraLarge
doubleExtraLarge
huge
*
*
* */