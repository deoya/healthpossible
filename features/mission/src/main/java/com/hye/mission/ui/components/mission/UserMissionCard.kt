package com.hye.mission.ui.components.mission

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.model.mission.MissionWithRecord
import com.hye.domain.model.mission.types.DayOfWeek
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseUnit
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.domain.model.mission.types.type
import com.hye.mission.ui.components.form.PrograssBar
import com.hye.mission.ui.util.UserMissionCardStyle
import com.hye.mission.ui.util.currentProgress
import com.hye.mission.ui.util.getDesign
import com.hye.mission.ui.util.getTargetString
import com.hye.mission.ui.util.notificationTimeString
import com.hye.shared.components.ui.StyledCard
import com.hye.shared.components.ui.StyledIconBox
import com.hye.shared.components.ui.StyledTag
import com.hye.shared.components.ui.Tag
import com.hye.shared.components.ui.TextBody
import com.hye.shared.components.ui.TextSubheading
import com.hye.shared.components.ui.common.IconStyle
import com.hye.shared.components.ui.common.completedColor
import com.hye.shared.components.ui.common.completedIcon
import com.hye.shared.components.ui.common.light
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.util.Calculator
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserMissionCard(
    missionWrapper: MissionWithRecord,
    onClick: () -> Unit = {}
) {
    val mission = missionWrapper.mission
    val record = missionWrapper.record

    var (color, secondColor, icon) = mission.type.getDesign()
    val isCompleted = record?.isCompleted ?: false
    val currentProgress = record?.progress ?: 0
    val targetText = mission.getTargetString()
    val currentText = "$currentProgress"
    val isInProgress = !isCompleted && currentProgress > 0

    val progressFloat = when (mission) {
        is RoutineMission -> Calculator.progress(mission.dailyTargetAmount, currentProgress)
        is ExerciseMission -> Calculator.progress(mission.targetValue, currentProgress)
        else -> if (isCompleted) 1f else 0f
    }

    StyledCard(
        style = UserMissionCardStyle(isCompleted, 1f)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(AppTheme.dimens.l)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.md)
            ) {
                // (1) 아이콘 박스
                StyledIconBox(
                    boxColor = isCompleted.completedColor(secondColor).light,
                    iconStyle = IconStyle(
                        image = isCompleted.completedIcon(icon),
                        tint = isCompleted.completedColor(color)
                    )
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxxs)
                ) {
                    Box(modifier = Modifier.padding(bottom = AppTheme.dimens.xxxxs)) {
                        StyledTag(
                            color = AppTheme.colors.backgroundMuted,
                            shape = AppTheme.dimens.xxxs,
                            horizontal = AppTheme.dimens.xxxs,
                            vertical = AppTheme.dimens.xxxxxs,
                            content = {
                                Icon(
                                    Icons.Outlined.Schedule,
                                    null,
                                    tint = AppTheme.colors.textSecondary,
                                    modifier = Modifier.size(AppTheme.dimens.s)
                                )
                            },
                            content2 = {
                                Tag(mission.notificationTimeString, color = AppTheme.colors.textSecondary)
                            }
                        )
                    }
                    TextSubheading(
                        text = mission.title,
                        size = AppTheme.dimens.m.toSp
                    )
                    TextBody(
                        text = isCompleted.currentProgress(currentText, targetText),
                        color = isCompleted.completedColor(
                            AppTheme.colors.textSecondary,
                            AppTheme.colors.mainColor
                        )
                    )
                }

                StartButton(
                    isInProgress = isInProgress && !isCompleted,
                    onClick = onClick
                )
            }

            if (isInProgress) {
                PrograssBar(progressFloat)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun UserMissionCard_Preview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. 진행 전 (물 마시기)
        UserMissionCard(
            missionWrapper = MissionWithRecord(
                mission = RoutineMission(
                    id = "1", title = "물 마시기", days = setOf(DayOfWeek.MON),
                    notificationTime = LocalTime.of(9, 0), dailyTargetAmount = 2000, amountPerStep = 250, unitLabel = "ml"
                ),
                record = null // 기록 없음
            )
        )

        // 2. 진행 중 (스쿼트)
        UserMissionCard(
            missionWrapper = MissionWithRecord(
                mission = ExerciseMission(
                    id = "2", title = "스쿼트 50회", days = setOf(DayOfWeek.MON),
                    notificationTime = LocalTime.of(18, 0), targetValue = 50, unit = ExerciseUnit.COUNT, useSupportAgent = false
                ),
                record = MissionRecord(
                    missionId = "2",
                    date = "2024-01-01",
                    progress = 25,
                    isCompleted = false
                )
            )
        )

        // 3. 완료됨
        UserMissionCard(
            missionWrapper = MissionWithRecord(
                mission = RoutineMission(
                    id = "3", title = "비타민 먹기", days = setOf(DayOfWeek.MON),
                    notificationTime = LocalTime.of(8, 0), dailyTargetAmount = 1, amountPerStep = 1, unitLabel = "회"
                ),
                record = MissionRecord(missionId = "3", date = "2024-01-01", progress = 1, isCompleted = true)
            )
        )
    }
}