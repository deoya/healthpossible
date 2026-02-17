package com.hye.shared.ui.button


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme


@Composable
fun SimplePillButton(
    modifier: Modifier = Modifier,
    // 1. 데이터 (Composable Content)
    content: @Composable () -> Unit,
    // 2. 스타일 설정
    containerHeight: Dp = AppTheme.dimens.bigDimen,
    containerColor: Color = AppTheme.colors.background,
    thumbSize: Dp = AppTheme.dimens.thumbSize,
    thumbColor: Color = AppTheme.colors.mainColor,
    thumbIcon: @Composable () -> Unit = {  },
    // 3. 동작 및 확장 (SlideButton에서 주입하기 위한 파라미터들)
    onClick: (() -> Unit)? = null, // null이면 클릭 안 됨
    thumbModifier: Modifier = Modifier, // 드래그 로직 주입용
    backgroundContent: @Composable () -> Unit = {} // 슬라이드 화살표 힌트 등
) {
    Box(
        modifier = modifier
            .height(containerHeight)
            .fillMaxWidth()
            .clip(RoundedCornerShape(containerHeight / 2))
            .background(containerColor)
            .padding(AppTheme.dimens.xxs)
    ) {
        // 1. 메인 컨텐츠 (텍스트 등) - 왼쪽 정렬
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = AppTheme.dimens.xxl, end = thumbSize + AppTheme.dimens.md),
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }

        // 2. 배경 요소 (확장 슬롯) - 오른쪽 정렬
        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            backgroundContent()
        }

        // 3. 썸 (동그라미 버튼)
        Box(
            contentAlignment = Alignment.Center,
            modifier = thumbModifier // 외부에서 주입된 Modifier (드래그 등)가 있다면 먼저 적용
                .align(Alignment.CenterEnd)
                .size(thumbSize)
                .clip(CircleShape)
                .background(thumbColor)
                .then(
                    // onClick이 있을 때만 클릭 가능하게 설정
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
                )
        ) {
            thumbIcon()
        }
    }
}

// [편의용 오버로딩] String 텍스트를 받는 SimplePillButton
@Composable
fun SimplePillButton(
    modifier: Modifier = Modifier,
    text: String,
    subText: String? = null,
    textColor: Color = AppTheme.colors.textPrimary,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    subTextColor: Color = AppTheme.colors.textSecondary,
    subTextStyle: TextStyle = MaterialTheme.typography.labelSmall,
    onClick: (() -> Unit)? = null,
    containerHeight: Dp = AppTheme.dimens.bigDimen,
    containerColor: Color = AppTheme.colors.background,
    thumbSize: Dp = AppTheme.dimens.thumbSize,
    thumbColor: Color = AppTheme.colors.mainColor,
    thumbIcon: @Composable () -> Unit = { }
) {
    SimplePillButton(
        modifier = modifier,
        content = {
            Column(verticalArrangement = Arrangement.Center) {
                if (!subText.isNullOrEmpty()) {
                    Text(subText, style = subTextStyle, color = subTextColor)
                }
                Text(text, style = textStyle, fontWeight = FontWeight.Bold, color = textColor)
            }
        },
        onClick = onClick,
        containerHeight = containerHeight,
        containerColor = containerColor,
        thumbSize = thumbSize,
        thumbColor = thumbColor,
        thumbIcon = thumbIcon
    )
}