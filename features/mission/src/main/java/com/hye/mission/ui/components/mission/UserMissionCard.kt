package com.hye.mission.ui.components.mission

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.hye.domain.model.mission.WeeklyMissionState
import com.hye.domain.model.mission.types.type
import com.hye.features.mission.R
import com.hye.mission.ui.util.UserMissionCardStyle
import com.hye.mission.ui.util.UserMissionTag
import com.hye.mission.ui.util.exerciseAgentMission
import com.hye.mission.ui.util.getDesign
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.common.completedBorder
import com.hye.shared.ui.common.completedColor
import com.hye.shared.ui.common.light
import com.hye.shared.ui.icon.StyledIconBox
import com.hye.shared.ui.text.LabelText
import com.hye.shared.ui.text.TitleText
import com.hye.shared.util.text

@Composable
fun UserMissionCard(
    state: WeeklyMissionState,
    onClick: () -> Unit = {},
    onNavigateToRecording: () -> Unit = {}
) {
    val mission = state.mission
    val isCompletedToday = state.isDoneToday
    val (color, secondColor, icon) = mission.type.getDesign()
    val isCompleted = state.completedCountThisWeek == mission.weeklyTargetCount
    UserMissionCardStyle (isCompleted)
    {
        Column(
            modifier = Modifier.padding(AppTheme.dimens.l),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.l)
        ) {

            // 1️⃣ 상단 정보 영역
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.s),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mission.notificationTime != null) {
                        UserMissionTag(
                            Icons.Outlined.Schedule,
                            /*Todo : mission.notificationTimeString 추가 */
                            ""
                        )
                    }
                    if (!mission.memo.isNullOrBlank()) {
                        /*Todo : 메모 혹은 미션 수정 버튼*/
                        Icon(
                            imageVector = Icons.Outlined.EditNote,
                            contentDescription = null,
                            tint = AppTheme.colors.textSecondary,
                            modifier = Modifier.size(AppTheme.dimens.m)
                        )
                    }
                }
            }

            // 2️⃣ 타이틀 & 아이콘
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.md)
            ) {
                StyledIconBox(
                    boxColor = (!isCompleted).completedColor(secondColor).light
                ) {
                    Icon(
                        imageVector =  icon,
                        contentDescription = null,
                        tint =  (!isCompleted).completedColor(color),
                        modifier = Modifier.size(AppTheme.dimens.xxl)
                    )
                }

                TitleText(text = mission.title)
            }

            // 3️⃣ 주간 진행도
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)
            ) {
                LabelText(
                    text = R.string.mission_current_result.text(mission.weeklyTargetCount,state.completedCountThisWeek),
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)
                ) {
                    repeat(mission.weeklyTargetCount) { index ->
                        val isFilled = index < state.completedCountThisWeek

                        Box(
                            modifier = Modifier
                                .size(AppTheme.dimens.xxl)
                                .clip(CircleShape)
                                .background(isFilled.completedColor( color))
                                .border(
                                    width = isFilled.completedBorder(),
                                    color = isFilled.completedColor(Color.Transparent),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isFilled) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = AppTheme.colors.background,
                                    modifier = Modifier.size(AppTheme.dimens.sm)
                                )
                            }
                        }
                    }
                }
            }

            // 4️⃣ 타입별 특화 영역
            Surface(
                color = AppTheme.colors.backgroundMuted,
                shape = RoundedCornerShape(AppTheme.dimens.s),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(AppTheme.dimens.md)) {
                    MissionTypeSpecificContent(state = state, color = color)
                }
            }

            // 5️⃣ 수행 버튼
            StartButton(
                isCompleted = isCompletedToday,
                onClick = {
                    if (mission.exerciseAgentMission) {
                        onNavigateToRecording()
                    } else {
                        onClick()
                    }
                }
            )
        }
    }
}
