package com.hye.domain.factory

import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.DayOfWeek
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RestrictionType
import com.hye.domain.model.mission.types.RoutineMission
import java.time.LocalTime
import java.util.UUID

object MissionFactory {
    data class MissionData(
        val title: String,
        val days: Set<DayOfWeek>,
        val memo: String? = null,
        val notificationTime: LocalTime? = null,

        val weeklyTargetCount: Int,
        val weekIdentifier: String? = null,
        val isTemplate: Boolean = false
    )

    fun createExerciseMission(
        data: MissionData,
        unit: ExerciseRecordMode,
        targetValue: Int,
        useSupportAgent: Boolean = false,
        selectedExercise: AiExerciseType? = null
    ): ExerciseMission = ExerciseMission(
        id = UUID.randomUUID().toString(),
        title = data.title,
        memo = data.memo,
        notificationTime = data.notificationTime,
        unit = unit,
        targetValue = targetValue,
        useSupportAgent = useSupportAgent,
        selectedExercise = selectedExercise,
        weeklyTargetCount = data.weeklyTargetCount,
        weekIdentifier = data.weekIdentifier,
        isTemplate = data.isTemplate
    )

    fun createDietMission(
        data: MissionData,
        recordMethod: DietRecordMethod
    ): DietMission = DietMission(
        id = UUID.randomUUID().toString(),
        title = data.title,
        memo = data.memo,
        notificationTime = data.notificationTime,
        recordMethod = recordMethod,
        weeklyTargetCount = data.weeklyTargetCount,
        weekIdentifier = data.weekIdentifier,
        isTemplate = data.isTemplate
    )

    fun createRoutineMission(
        data: MissionData,
        dailyTargetAmount: Int,
        amountPerStep: Int,
        unitLabel: String
    ): RoutineMission = RoutineMission(
        id = UUID.randomUUID().toString(),
        title = data.title,
        memo = data.memo,
        notificationTime = data.notificationTime,
        dailyTargetAmount = dailyTargetAmount,
        amountPerStep = amountPerStep,
        unitLabel = unitLabel,
        weeklyTargetCount = data.weeklyTargetCount,
        weekIdentifier = data.weekIdentifier,
        isTemplate = data.isTemplate
    )

    fun createRestrictionMission(
        data: MissionData,
        type: RestrictionType,
        maxAllowedMinutes: Int?
    ): RestrictionMission = RestrictionMission(
        id = UUID.randomUUID().toString(),
        title = data.title,
        memo = data.memo,
        notificationTime = data.notificationTime,
        type = type,
        maxAllowedMinutes = maxAllowedMinutes,
        weeklyTargetCount = data.weeklyTargetCount,
        weekIdentifier = data.weekIdentifier,
        isTemplate = data.isTemplate
    )
}