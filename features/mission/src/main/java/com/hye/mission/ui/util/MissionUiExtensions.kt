package com.hye.mission.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.hye.domain.model.mission.DietMission
import com.hye.domain.model.mission.DietRecordMethod
import com.hye.domain.model.mission.ExerciseMission
import com.hye.domain.model.mission.Mission
import com.hye.domain.model.mission.MissionCategory
import com.hye.domain.model.mission.RestrictionMission
import com.hye.domain.model.mission.RoutineMission

data class MissionAppearance(val color: Color, val containerColor: Color, val icon: ImageVector)

fun Mission.getIconDesign(): MissionAppearance {
    return when (this) {
        is ExerciseMission -> MissionAppearance(
            Color(0xFF1168EC),
            Color(0xFF8FBEF2),
            Icons.Filled.FitnessCenter
        )

       is DietMission -> MissionAppearance(
            Color(0xFF905EEF),
            Color(0xFFCDB6FC),
            Icons.Filled.Restaurant
        )

        is RoutineMission -> MissionAppearance(
            Color(0xFFF3B128),
            Color(0xFFF6DFB4),
            Icons.Filled.WbSunny
        )

        is RestrictionMission -> MissionAppearance(
            Color(0xFFEB5DCC),
            Color(0xFFF4B0EB),
            Icons.Filled.Block
        )
    }
}
data class DietRecordMethodAppearance(val text: String, val icon: ImageVector, val alert: String)

val DietRecordMethod.getAppearance :DietRecordMethodAppearance
    get() = when (this) {
    DietRecordMethod.PHOTO -> DietRecordMethodAppearance("사진 인증", Icons.Default.CameraAlt,"식사 전/후 사진을 찍어 갤러리 형태로 기록합니다.")
    DietRecordMethod.TEXT -> DietRecordMethodAppearance("식단 메모",Icons.Default.Edit,"먹은 메뉴와 칼로리 등을 텍스트로 상세히 적습니다.")
    DietRecordMethod.CHECK -> DietRecordMethodAppearance("수행 체크",Icons.Default.CheckCircle,"식단을 잘 지켰는지 O/X로 간단하게 체크합니다.")
}

