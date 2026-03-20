package com.hye.data.mapper

import com.hye.data.dto.MissionDto
import com.hye.data.dto.MissionRecordDto
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.MissionType
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RestrictionType
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.domain.model.mission.types.type
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun MissionDto.toDomain(): Mission {
    return when (MissionType.valueOf(this.type)) {
        MissionType.EXERCISE -> this.toExerciseMission()
        MissionType.DIET -> this.toDietMission()
        MissionType.ROUTINE -> this.toRoutineMission()
        MissionType.RESTRICTION -> this.toRestrictionMission()
    }
}

// 🔥 요일(days) 관련 함수 제거됨

private fun MissionDto.getCommonNotificationTime(): LocalTime? =
    this.notificationTime?.let { LocalTime.parse(it, DateTimeFormatter.ISO_LOCAL_TIME) }

private fun MissionDto.toExerciseMission(): ExerciseMission {
    return ExerciseMission(
        id = this.id,
        title = this.title,
        memo = this.memo,
        notificationTime = this.getCommonNotificationTime(),

        // 🔥 하드코딩 제거 및 새로운 공통 필드 매핑
        weeklyTargetCount = this.weeklyTargetCount ?: 1,
        weekIdentifier = this.weekIdentifier,
        isTemplate = this.isTemplate ?: false,

        unit = this.unit?.let { ExerciseRecordMode.valueOf(it) } ?: ExerciseRecordMode.SELECTED,
        selectedExercise = this.selectedExercise?.let { AiExerciseType.valueOf(it) },
        targetValue = this.targetValue ?: 0,
        useSupportAgent = this.useSupportAgent ?: false
    )
}

private fun MissionDto.toDietMission(): DietMission {
    return DietMission(
        id = this.id,
        title = this.title,
        memo = this.memo,
        notificationTime = this.getCommonNotificationTime(),

        // 🔥 새로운 공통 필드 매핑
        weeklyTargetCount = this.weeklyTargetCount ?: 1,
        weekIdentifier = this.weekIdentifier,
        isTemplate = this.isTemplate ?: false,

        recordMethod = this.recordMethod?.let { DietRecordMethod.valueOf(it) } ?: DietRecordMethod.CHECK
    )
}

private fun MissionDto.toRoutineMission(): RoutineMission {
    return RoutineMission(
        id = this.id,
        title = this.title,
        memo = this.memo,
        notificationTime = this.getCommonNotificationTime(),

        // 🔥 새로운 공통 필드 매핑
        weeklyTargetCount = this.weeklyTargetCount ?: 1,
        weekIdentifier = this.weekIdentifier,
        isTemplate = this.isTemplate ?: false,

        dailyTargetAmount = this.dailyTargetAmount ?: 0,
        amountPerStep = this.amountPerStep ?: 0,
        unitLabel = this.unitLabel ?: ""
    )
}

private fun MissionDto.toRestrictionMission(): RestrictionMission {
    return RestrictionMission(
        id = this.id,
        title = this.title,
        memo = this.memo,
        notificationTime = this.getCommonNotificationTime(),

        // 🔥 새로운 공통 필드 매핑
        weeklyTargetCount = this.weeklyTargetCount ?: 1,
        weekIdentifier = this.weekIdentifier,
        isTemplate = this.isTemplate ?: false,

        type = this.restrictionType?.let { RestrictionType.valueOf(it) } ?: RestrictionType.CHECK,
        maxAllowedMinutes = this.maxAllowedMinutes
    )
}

fun Mission.toDto(): MissionDto {
    val baseDto = MissionDto(
        id = this.id,
        uid = "", // 💡 DataSource나 Repository에서 현재 유저의 UID를 덮어씌워야 합니다!
        title = this.title,
        type = this.type.name,
        notificationTime = this.notificationTime?.format(DateTimeFormatter.ISO_LOCAL_TIME),
        memo = this.memo,

        // 🔥 새로운 공통 필드 매핑 (days 리스트 제거)
        weeklyTargetCount = this.weeklyTargetCount,
        weekIdentifier = this.weekIdentifier,
        isTemplate = this.isTemplate
    )

    return when (this) {
        is ExerciseMission -> baseDto.copy(
            unit = this.unit.name,
            selectedExercise = this.selectedExercise?.name,
            targetValue = this.targetValue,
            useSupportAgent = this.useSupportAgent
        )
        is DietMission -> baseDto.copy(
            recordMethod = this.recordMethod.name
        )
        is RoutineMission -> baseDto.copy(
            dailyTargetAmount = this.dailyTargetAmount,
            amountPerStep = this.amountPerStep,
            unitLabel = this.unitLabel
        )
        is RestrictionMission -> baseDto.copy(
            restrictionType = this.type.name,
            maxAllowedMinutes = this.maxAllowedMinutes
        )
    }
}

fun MissionRecordDto.toRecordDomain(): MissionRecord {
    return MissionRecord(
        id = this.id,
        missionId = this.missionId,
        date = LocalDate.parse(this.date),
        progress = this.progress,
        isCompleted = this.isCompleted,
        completedAt = this.completedAt
    )
}

fun MissionRecord.toRecordDto(): MissionRecordDto {
    return MissionRecordDto(
        id = this.id,
        uid = "", // 💡 DataSource나 Repository에서 현재 유저의 UID를 덮어씌워야 합니다!
        missionId = this.missionId,
        date = this.date.toString(),
        progress = this.progress,
        isCompleted = this.isCompleted,
        completedAt = this.completedAt
    )
}