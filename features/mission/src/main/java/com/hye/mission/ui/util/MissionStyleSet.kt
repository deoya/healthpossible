package com.hye.mission.ui.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hye.shared.components.ui.ButtonStyle
import com.hye.shared.components.ui.CardStyle
import com.hye.shared.components.ui.SwitchStyle
import com.hye.shared.theme.AppTheme


val dailyProgressCardStyle: CardStyle
    @Composable
    get() = CardStyle(
        shape = RoundedCornerShape(size = AppTheme.dimens.extraLarge),
        containerColor = AppTheme.colors.mainColor,
        elevation = AppTheme.dimens.tiny
    )

val Boolean.selectionBorderColor: Color
    @Composable
    get() = if (this) AppTheme.colors.mainColor else Color.Transparent

val Boolean.selectionIconMenuColor: Color
    @Composable
    get() = if (this) AppTheme.colors.mainColor else AppTheme.colors.textSecondary

val Boolean.selectionFontWeight: FontWeight
    @Composable
    get() = if (this)FontWeight.Bold else FontWeight.Medium


//val Boolean.selectionContainerColor: Color
//    @Composable
//    get() = if (this) AppTheme.colors.mainColorLight else AppTheme.colors.backgroundMuted

@Composable
fun UserMissionCardStyle(isCompleted: Boolean, cardAlpha: Float): CardStyle =
    CardStyle(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppTheme.dimens.mediumSmall)
            .clickable { /* Todo 상세 보기 or 기록하기 */ },
        containerColor = AppTheme.colors.background.copy(alpha = cardAlpha),
        elevation = if (isCompleted) 0.dp else AppTheme.dimens.micro,
        border = if (isCompleted) BorderStroke(0.dp, Color.Transparent) else null,
    )

@Composable
fun StartButtonStyle(onClick: () -> Unit, isInProgress: Boolean): ButtonStyle =
    ButtonStyle(
        onClick = onClick,
        shape = RoundedCornerShape(AppTheme.dimens.mediumSmall),
        containerColor = if (isInProgress) AppTheme.colors.mainColorLight else AppTheme.colors.mainColor,
        contentColor = if (isInProgress) AppTheme.colors.mainColor else Color.White,
        elevation = 0.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.height(36.dp)
    )

@Composable
fun UserSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit): SwitchStyle = SwitchStyle(
    checked = checked,
    onCheckedChange = onCheckedChange,
    checkedThumbColor = AppTheme.colors.selectedLabelColor,
    checkedTrackColor = AppTheme.colors.mainColor,
    uncheckedThumbColor = AppTheme.colors.selectedLabelColor,
    uncheckedTrackColor = AppTheme.colors.uncheckedTrackColor
)
