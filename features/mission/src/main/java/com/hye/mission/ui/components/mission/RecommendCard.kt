package com.hye.mission.ui.components.mission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.type
import com.hye.features.mission.R
import com.hye.mission.ui.util.getDesign
import com.hye.mission.ui.util.getRecommendDescription
import com.hye.mission.ui.util.getRecommendTarget
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.ui.button.PrimaryButton
import com.hye.shared.ui.button.SecondaryButton
import com.hye.shared.ui.card.DecisionCard
import com.hye.shared.ui.icon.IconTextBadge
import com.hye.shared.ui.icon.StyledIconBox
import com.hye.shared.ui.text.BodyText
import com.hye.shared.ui.text.TextStyleSize
import com.hye.shared.ui.text.TitleText
import com.hye.shared.util.DateFormatType
import com.hye.shared.util.getFormattedTime
import com.hye.shared.util.text

@Composable
fun RecommendCard(
    mission: Mission,
    onAccept: (Mission) -> Unit,
    onReject: (Mission) -> Unit
) {
    val (color, secondColor, icon) = mission.type.getDesign()

    DecisionCard(
        modifier = Modifier
            .width(AppTheme.dimens.decisionCardWidth)
            .wrapContentHeight(),
        leadingIcon = {
            StyledIconBox(
                boxColor = secondColor
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(AppTheme.dimens.xxl)
                )
            }
        },
        trailingBadge = {
            Row (horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxs)) {
                if (mission.notificationTime != null) {
                    val timeStr = getFormattedTime(mission.notificationTime!!, DateFormatType.SIMPLE_TIME)
                    IconTextBadge(icon = Icons.Outlined.Timer, text = timeStr)
                }

                if (mission is ExerciseMission && mission.useSupportAgent) {
                    IconTextBadge(icon = Icons.Outlined.AutoAwesome, text = R.string.mission_recommend_ai_coaching.text)
                }
            }
        },
        title = {
            TitleText(
                text = mission.title,
                style = TextStyleSize.Large,
                fontWeight = FontWeight.ExtraBold,
            )
        },
        description = {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)) {
                // 1. 구체적인 목표치
                TitleText(
                    text = R.string.mission_recommend_target_format.text(mission.getRecommendTarget()),
                    style = TextStyleSize.Small,
                    color = color
                )

                // 2. 부연 설명 및 메모
                BodyText(
                    text = mission.getRecommendDescription(),
                    color = AppTheme.colors.textSecondary,
                    lineHeight = AppTheme.dimens.l.toSp,
                    maxLines = 3
                )
            }
        },
        actions = {
            SecondaryButton(
                text = R.string.mission_recommend_postpone.text,
                onClick = { onReject(mission) },
                modifier = Modifier.weight(1f)
            )
            PrimaryButton(
                text = R.string.mission_recommend_accept.text,
                onClick = { onAccept(mission) },
                modifier = Modifier.weight(1f)
            )
        }
    )
}
