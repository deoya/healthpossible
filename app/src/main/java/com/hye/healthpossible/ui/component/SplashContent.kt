package com.hye.healthpossible.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hye.shared.theme.AppTheme

@Composable
fun SplashContent(
    logo : @Composable () -> Unit = {},
    title : @Composable () -> Unit = {},
    copyright : @Composable () -> Unit = {},
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.offset(y = (AppTheme.dimens.xxxl)*-1)
            ) {
                logo()
                title()
            }
            Box(modifier = Modifier.align(Alignment.BottomCenter)){
                copyright()
            }
        }
    }
}