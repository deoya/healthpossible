package com.hye.profile.ui.components.survey

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.hye.shared.theme.AppTheme

// Todo : 공통 모듈로 UI 추출
@Composable
fun SurveyProgressIndicator(
    totalSteps: Int,
    currentStep: Int,
    completionStatus: List<Boolean>,
    onStepClick: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.s),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            val isCurrent = index == currentStep
            val isCompleted = completionStatus.getOrElse(index) { false }

            val size by animateDpAsState(if (isCurrent) AppTheme.dimens.md else AppTheme.dimens.s, label = "size")
            val color by animateColorAsState(
                when {
                    isCompleted -> AppTheme.colors.completeColor
                    isCurrent -> AppTheme.colors.mainColorLight
                    else -> AppTheme.colors.mainColorLight
                }, label = "color"
            )

            Box(
                modifier = Modifier
                    .size(AppTheme.dimens.xxxxl)
                    .clip(CircleShape)
                    .clickable { onStepClick(index) },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .background(color, CircleShape)
                        .then(
                            if (isCurrent && !isCompleted) Modifier.border(
                                AppTheme.dimens.xxxxxs,
                                AppTheme.colors.mainColor,
                                CircleShape
                            ) else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = AppTheme.colors.mainColorLight,
                            modifier = Modifier.size(if(isCurrent) AppTheme.dimens.xs else AppTheme.dimens.xxs)
                        )
                    }
                }
            }
        }
    }
}