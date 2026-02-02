package com.hye.mission.ui.components.recording.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BaseSessionLayout(
    modifier: Modifier = Modifier,
    backgroundContent: @Composable BoxScope.() -> Unit, // 배경 슬롯 (카메라, 지도 등)
    topBarContent: @Composable BoxScope.() -> Unit,     // 상단 슬롯 (뒤로가기, 타이머 등)
    bottomBarContent: @Composable BoxScope.() -> Unit   // 하단 슬롯 (컨트롤, 피드백 등)
) {
    Box(modifier = modifier.fillMaxSize()) {
        // 1. 배경 레이어
        backgroundContent()

        // 2. 상단 레이어
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            topBarContent()
        }

        // 3. 하단 레이어
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            bottomBarContent()
        }
    }
}