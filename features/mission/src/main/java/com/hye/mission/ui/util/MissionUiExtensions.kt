package com.hye.mission.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.MissionType
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.features.mission.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.text

data class MissionAppearance(val color: Color, val secondColor : Color, val icon: ImageVector)


@Composable
fun MissionType.getDesign(): MissionAppearance = when (this) {
    MissionType.EXERCISE -> MissionAppearance(
        AppTheme.colors.exerciseColor,
        AppTheme.colors.exerciseSecondColor,
        Icons.Filled.FitnessCenter
    )

    MissionType.DIET -> MissionAppearance(
        AppTheme.colors.dietColor,
        AppTheme.colors.dietSecondColor,
        Icons.Filled.Restaurant
    )

    MissionType.ROUTINE -> MissionAppearance(
        AppTheme.colors.routineColor,
        AppTheme.colors.routineSecondColor,
        Icons.Filled.WbSunny
    )

    MissionType.RESTRICTION -> MissionAppearance(
        AppTheme.colors.restrictionColor,
        AppTheme.colors.restrictionSecondColor,
        Icons.Filled.Block
    )
}

data class DietRecordMethodAppearance(val text: String, val icon: ImageVector, val alert: String)

val DietRecordMethod.getAppearance: DietRecordMethodAppearance
    @Composable
    get() = when (this) {
        DietRecordMethod.PHOTO -> DietRecordMethodAppearance(
            R.string.mission_plan_diet_record_photo.text,
            Icons.Default.CameraAlt,
            R.string.mission_plan_diet_record_photo_description.text
        )

        DietRecordMethod.TEXT -> DietRecordMethodAppearance(
            R.string.mission_plan_diet_record_text.text,
            Icons.Default.Edit,
            R.string.mission_plan_diet_record_text_description.text
        )

        DietRecordMethod.CHECK -> DietRecordMethodAppearance(
            R.string.mission_plan_diet_record_check.text,
            Icons.Default.CheckCircle,
            R.string.mission_plan_diet_record_check_description.text
        )
    }

@Composable
fun Mission.getTargetString(): String = when (this) {
    is ExerciseMission -> "$targetValue ${unit.label}"
    is DietMission -> recordMethod.name
    is RoutineMission -> "$dailyTargetAmount $unitLabel"
    is RestrictionMission -> if (maxAllowedMinutes != null) R.string.mission_plan_daily_limit_minute.text(maxAllowedMinutes!!) else R.string.mission_forbid.text
}

val Float.cheerMessage: String
    @Composable get() {
        val messages = stringArrayResource(id = R.array.mission_cheer_messages)
        val validProgress = this.coerceIn(0f, 1f)
        val rawIndex = (validProgress * messages.size).toInt()
        val index = rawIndex.coerceAtMost(messages.lastIndex)

        return messages[index]
    }
