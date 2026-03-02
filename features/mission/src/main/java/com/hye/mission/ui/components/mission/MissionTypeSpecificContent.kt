package com.hye.mission.ui.components.mission

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.hye.domain.model.mission.WeeklyMissionState
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.features.mission.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.text.BodyText
import com.hye.shared.ui.text.LabelText
import com.hye.shared.util.text

// 미션 도메인 타입에 따라 내부 컨텐츠를 다르게 그리는 컴포저블
@Composable
fun MissionTypeSpecificContent(
    state: WeeklyMissionState,
    color: Color
) {
    when (val mission = state.mission) {

        is ExerciseMission -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BodyText(
                    text = "${mission.targetValue} ${mission.unit.label}",
                    fontWeight = FontWeight.Bold
                )

                if (mission.useSupportAgent) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxs)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AppTheme.colors.supportAgentColor,
                            modifier = Modifier.size(AppTheme.dimens.md)
                        )
                        LabelText(
                            text = R.string.mission_card_ai_support.text,
                            color = AppTheme.colors.supportAgentColor
                        )
                    }
                }
            }
        }

        is DietMission -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(AppTheme.dimens.l)
                )

                BodyText(
                    text = R.string.mission_card_diet_record_method
                        .text(mission.recordMethod.name),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        is RoutineMission -> {
            val progressRatio = animateFloatAsState(
                targetValue = (
                        state.todayProgress.toFloat() /
                                mission.dailyTargetAmount.toFloat()
                        ).coerceIn(0f, 1f),
                label = "progress"
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LabelText(
                        text = R.string.mission_card_today_progress.text,
                        color = AppTheme.colors.textSecondary
                    )
                    LabelText(
                        text = "${state.todayProgress} / ${mission.dailyTargetAmount} ${mission.unitLabel}",
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.dimens.xxs)
                        .clip(RoundedCornerShape(AppTheme.dimens.xxxxs))
                        .background(Color(0xFFE2E8F0))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progressRatio.value)
                            .background(
                                color = color,
                                shape = RoundedCornerShape(AppTheme.dimens.xxxxs)
                            )
                    )
                }
            }
        }

        is RestrictionMission -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.HourglassEmpty,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(AppTheme.dimens.l)
                    )
                    BodyText(
                        text = R.string.mission_card_restriction_method
                            .text(mission.type.name),
                        fontWeight = FontWeight.Bold
                    )
                }

                mission.maxAllowedMinutes?.let {
                    LabelText(
                        text = R.string.mission_card_allowed_time.text(it),
                        color = color,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}