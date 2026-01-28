package com.hye.mission.ui.components.mission

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.hye.features.mission.R
import com.hye.mission.ui.util.cheerMessage
import com.hye.mission.ui.util.dailyProgressCardStyle
import com.hye.shared.components.ui.DisplayTextSmall
import com.hye.shared.components.ui.StyledCard
import com.hye.shared.components.ui.TextBody
import com.hye.shared.components.ui.TitleMedium
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.Calculator
import com.hye.shared.util.text

// ✅ 1. 레이아웃 (Layout)
@Composable
fun DailyProgressCardLayout(
    messageContent: @Composable () -> Unit,
    progressTextContent: @Composable () -> Unit,
    resultTextContent: @Composable () -> Unit,
    indicatorContent: @Composable () -> Unit
) {
    StyledCard(dailyProgressCardStyle) {
        Row(
            modifier = Modifier.padding(AppTheme.dimens.xxl),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxs)
            ) {
                Surface(
                    color = AppTheme.colors.background.copy(alpha = AppTheme.dimens.alphaMuted),
                    shape = RoundedCornerShape(AppTheme.dimens.xxs),
                ) {
                    Box(modifier = Modifier.padding(horizontal = AppTheme.dimens.xxs, vertical = AppTheme.dimens.xxxxs)) {
                        messageContent()
                    }
                }
                progressTextContent()
                resultTextContent()
            }

            Box(contentAlignment = Alignment.Center) {
                indicatorContent()
            }
        }
    }
}

// ✅ 2. 구현체 (Stateful)
@Composable
fun DailyProgressCard(
    totalCount: Int,
    completedCount: Int
) {
    val progress = Calculator.progress(totalCount, completedCount)
    val animatedProgress by animateFloatAsState(targetValue = progress)
    val cheerMessage = progress.cheerMessage

    DailyProgressCardLayout(
        messageContent = {
            TitleMedium(cheerMessage, color = AppTheme.colors.background)
        },
        progressTextContent = {
            DisplayTextSmall(R.string.mission_current_progress.text((progress * 100).toInt()))
        },
        resultTextContent = {
            TextBody(
                R.string.mission_current_result.text(completedCount, totalCount),
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.background.copy(alpha = AppTheme.dimens.alphaPrimary)
            )
        },
        indicatorContent = {
            // 배경 원
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(AppTheme.dimens.progressSize),
                color = AppTheme.colors.background.copy(alpha = AppTheme.dimens.alphaMuted),
                strokeWidth = AppTheme.dimens.xxs,
            )
            // 진행 원
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.size(AppTheme.dimens.progressSize),
                color = AppTheme.colors.background,
                strokeWidth = AppTheme.dimens.xxs,
                trackColor = AppTheme.colors.incompleteColor,
                strokeCap = StrokeCap.Round
            )
            // 아이콘
            Icon(
                painter = painterResource(R.drawable.wireless),
                contentDescription = null,
                tint = AppTheme.colors.background,
                modifier = Modifier.size(AppTheme.dimens.xxxxxl)
            )
        }
    )
}
@Preview
@Composable
fun DailyProgressCard_Preview(
) {
    DailyProgressCard(
        totalCount = 10,
        completedCount = 4
    )
}