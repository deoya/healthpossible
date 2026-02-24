package com.hye.healthpossible.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OnboardingScreen(
    pagerState: PagerState,
    authSlot: @Composable () -> Unit,      // 첫 번째 페이지 슬롯
    strategySlot: @Composable () -> Unit,  // 두 번째 페이지 슬롯
    surveySlot: @Composable () -> Unit     // 세 번째 페이지 슬롯
) {
    // BaseScreenTemplate은 상위 Route나 개별 화면에서 감싸는 것이 좋지만,
    // 전체 온보딩의 틀을 유지하려면 여기에 둘 수도 있습니다.
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false, // 스와이프 방지 (버튼으로만 이동)
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> authSlot()
            1 -> strategySlot()
            2 -> surveySlot()
        }
    }
}