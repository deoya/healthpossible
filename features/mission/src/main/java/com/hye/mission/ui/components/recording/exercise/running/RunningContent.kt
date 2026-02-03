package com.hye.mission.ui.components.recording.exercise.running

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.hye.features.mission.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.ui.button.SimplePillButton
import com.hye.shared.ui.common.toPlayPauseIcon
import com.hye.shared.ui.progress.CircularProgressBar
import com.hye.shared.ui.text.LabelMedium
import com.hye.shared.ui.text.TitleMedium
import com.hye.shared.util.Calculator
import com.hye.shared.util.text

@Composable
fun RunningContent(
    totalTargetValue : String,
    currentProgressValue : String,
    isRunning : Boolean,
    currentStep : Int,
    onToggleTimer: () -> Unit,
){

    val animatedProgress by animateFloatAsState(
        targetValue = Calculator.progress(
            totalTargetValue,
            currentProgressValue
        )
    )


    SimplePillButton(
        containerHeight = AppTheme.dimens.runnigSimplePillHeight,
        modifier = Modifier,
        content = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xs)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxxs)
                    ) {
                        Icon(
                            Icons.Default.DirectionsRun,
                            null,
                            tint = AppTheme.colors.mainColor,
                            modifier = Modifier.size(AppTheme.dimens.l)
                        )
                        LabelMedium(
                            text = R.string.mission_step_count.text,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = currentStep.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppTheme.colors.textPrimary,
                        fontSize = AppTheme.dimens.xxxl.toSp
                    )
                    //Todo: 칼로리 또는 거리를 추가 할 것
                }
            }
        },
        thumbColor = AppTheme.colors.InheritColor,
        thumbSize = AppTheme.dimens.RunningCircularProgressSize,
        thumbIcon = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {onToggleTimer() }
            ) {
                CircularProgressBar(
                    progress = animatedProgress,
                    color = AppTheme.colors.supportAgentColor,
                    size = AppTheme.dimens.RunningCircularProgressSize,
                    iconContent = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxxs)
                        ) {
                            TitleMedium(
                                text = currentProgressValue,
                                color = AppTheme.colors.supportAgentColor,
                                size = AppTheme.dimens.m.toSp
                            )
                            Icon(
                                imageVector = isRunning.toPlayPauseIcon,
                                contentDescription = null,
                                tint = AppTheme.colors.supportAgentColor,
                                modifier = Modifier.size(AppTheme.dimens.xxl)
                            )
                        }
                    }
                )
            }
        }
    )
}