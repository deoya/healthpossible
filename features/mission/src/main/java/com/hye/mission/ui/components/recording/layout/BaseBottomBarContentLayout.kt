package com.hye.mission.ui.components.recording.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hye.shared.theme.AppTheme


@Composable
fun BaseBottomBarContentLayout(
    content: @Composable () -> Unit,
    bottomSheetContent : @Composable BoxScope.() -> Unit = {}
){
    Column(
        modifier = Modifier.padding(AppTheme.dimens.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = AppTheme.dimens.xxxxxl,
                        start = AppTheme.dimens.xxl,
                        end = AppTheme.dimens.xxl
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.md)
            ) {
                content()
            }
            bottomSheetContent()
        }
    }
}