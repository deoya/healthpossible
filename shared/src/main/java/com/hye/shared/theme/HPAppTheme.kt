package com.hye.shared.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun HPAppTheme(
    /*Todo 다크모드 추가할 것*/
    content:@Composable () -> Unit
){
    val dimens = HPAppDimens()

    CompositionLocalProvider(
        LocalDimens provides dimens,
        LocalColors provides LightColors
    ) {
        content()
    }
}

object AppTheme{
    val dimens: HPAppDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimens.current

    val colors: HPAppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current
}