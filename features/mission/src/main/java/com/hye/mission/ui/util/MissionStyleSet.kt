package com.hye.mission.ui.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.RestrictionType
import com.hye.features.mission.R
import com.hye.shared.ui.CardStyle
import com.hye.shared.ui.button.SwitchStyle
import com.hye.shared.ui.common.completedBorder
import com.hye.shared.ui.common.completedElevation
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.DateFormatType
import com.hye.shared.util.text
import java.time.format.DateTimeFormatter


val Mission.notificationTimeString: String
    @RequiresApi(Build.VERSION_CODES.O) @Composable
    get() = this.notificationTime?.format(
            DateTimeFormatter.ofPattern(DateFormatType.SIMPLE_TIME.pattern)
        ) ?: R.string.mission_all_the_time.text

@Composable
fun Boolean.currentProgress(crrent: String, target: String) : String =
    if (this) R.string.mission_completed.text else "$crrent / $target"


val RestrictionType.choiceRule : Int
    @Composable
    get() = when(this) {
        RestrictionType.TIMER -> R.string.mission_plan_limit_type_time
        else -> R.string.mission_plan_limit_type_count
    }

val RestrictionType.isTimer : Boolean
    get() = this == RestrictionType.TIMER


val dailyProgressCardStyle: CardStyle
    @Composable
    get() = CardStyle(
        shape = RoundedCornerShape(size = AppTheme.dimens.l),
        containerColor = AppTheme.colors.mainColor,
        elevation = AppTheme.dimens.xxxxs
    )


@Composable
fun UserMissionCardStyle(isCompleted: Boolean, cardAlpha: Float): CardStyle =
    CardStyle(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppTheme.dimens.s)
            .clickable { /* Todo 상세 보기 or 기록하기 */ },
        containerColor = AppTheme.colors.background.copy(alpha = cardAlpha),
        elevation = isCompleted.completedElevation(),
        border = isCompleted.completedBorder(),
        shape = RoundedCornerShape(AppTheme.dimens.l)
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
