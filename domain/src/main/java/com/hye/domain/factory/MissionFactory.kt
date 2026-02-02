package com.hye.domain.factory

import com.hye.domain.model.mission.types.DayOfWeek
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.MissionType
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RestrictionType
import com.hye.domain.model.mission.types.RoutineMission
import java.time.LocalTime

object MissionFactory {
    fun create(
        id: String,
        type: MissionType,
        title: String,
        days: Set<DayOfWeek>,
        memo: String?,
        notificationTime: LocalTime?,
    ): Mission {
        return when (type) {
            MissionType.EXERCISE -> ExerciseMission(
                id = id, title = title, days = days, memo = memo, notificationTime = notificationTime,
                targetValue = 0, unit = ExerciseRecordMode.RUNNING, useSupportAgent = false
            )
            MissionType.DIET -> DietMission(
                id = id, title = title, days = days, memo = memo, notificationTime = notificationTime,
                recordMethod = DietRecordMethod.TEXT
            )
            MissionType.ROUTINE -> RoutineMission(
                id = id, title = title, days = days, memo = memo, notificationTime = notificationTime,
                dailyTargetAmount = 0, amountPerStep = 0, unitLabel = ""
            )
            MissionType.RESTRICTION -> RestrictionMission(
                id = id, title = title, days = days, memo = memo, notificationTime = notificationTime,
                type = RestrictionType.CHECK, maxAllowedMinutes = null
            )
        }
    }
}