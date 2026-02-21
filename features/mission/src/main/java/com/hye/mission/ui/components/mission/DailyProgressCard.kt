package com.hye.mission.ui.components.mission

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.hye.features.mission.R
import com.hye.mission.ui.util.cheerMessage
import com.hye.mission.ui.util.dailyProgressCardStyle
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.ui.StyledCard
import com.hye.shared.ui.progress.CircularProgressBar
import com.hye.shared.ui.text.DisplayTextSmall
import com.hye.shared.ui.text.TextBody
import com.hye.shared.ui.text.TitleMedium
import com.hye.shared.util.Calculator
import com.hye.shared.util.text

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
            TitleMedium(cheerMessage, color = AppTheme.colors.background, size = AppTheme.dimens.sm.toSp)
        },
        progressTextContent = {
            DisplayTextSmall(R.string.mission_current_progress.text((progress * 100).toInt()), color = AppTheme.colors.background)
        },
        resultTextContent = {
            TextBody(
                R.string.mission_current_result.text(completedCount, totalCount),
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.background.copy(alpha = AppTheme.dimens.alphaPrimary)
            )
        },
        indicatorContent = {
            CircularProgressBar(
                progress = animatedProgress,
                iconContent = {
                    Icon(
                        painter = painterResource(R.drawable.wireless),
                        contentDescription = null,
                        tint = AppTheme.colors.background,
                        modifier = Modifier.size(AppTheme.dimens.xxxxxxl)
                    )
                }
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