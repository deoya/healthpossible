package com.hye.shared.ui.common

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import com.hye.shared.theme.AppTheme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Composable
fun CommonBottomSheet(
    modifier: Modifier = Modifier,
    sheetHeightFraction: Float = 0.6f,
    containerColor: Color = AppTheme.colors.background,
    shape: Shape = RoundedCornerShape(topStart = AppTheme.dimens.xxxxl, topEnd = AppTheme.dimens.xxxxl),
    elevation: Dp = AppTheme.dimens.md,

    showDragHandle: Boolean = true,
    onDismissRequest: () -> Unit = {},
    header: @Composable (() -> Unit)? = null,
    bottomArea: @Composable (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    BackHandler (onBack = onDismissRequest)

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(), // 전체 화면 너비만 채움 (높이는 wrap)
        contentAlignment = Alignment.BottomCenter
    ) {
        val screenHeight = maxHeight
        // 최대 높이 계산
        val maxSheetHeight = screenHeight * sheetHeightFraction

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxSheetHeight),
            shape = shape,
            color = containerColor,
            shadowElevation = elevation
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                // 1. 드래그 핸들 & 헤더
                if (showDragHandle) BottomSheetDragHandle()

                if (header != null) header()

                // 2. 메인 컨텐츠
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) {
                    content()
                }

                // 3. 하단 버튼 영역
                if (bottomArea != null) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        bottomArea()
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetDragHandle(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.divider
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimens.md),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(AppTheme.dimens.xxxxxxl)
                .height(AppTheme.dimens.xxxxs)
                .clip(RoundedCornerShape(AppTheme.dimens.xxxxxs))
                .background(color)
        )
    }
}