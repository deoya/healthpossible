
package com.hye.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.DayOfWeek
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RestrictionType
import com.hye.domain.model.mission.types.RoutineMission
import java.time.LocalTime
import java.time.format.DateTimeFormatter


// [저장용] Mission 객체 -> Map 변환
@RequiresApi(Build.VERSION_CODES.O)
fun missionToMap(mission: Mission): Map<String, Any?> {
    // 1. 공통 필드 처리
    val commonData = mutableMapOf<String, Any?>(
        "id" to mission.id,
        "title" to mission.title,
        "days" to mission.days.map { it.name }, // Enum -> String 리스트
        "memo" to mission.memo,
        "notificationTime" to mission.notificationTime?.format(DateTimeFormatter.ISO_LOCAL_TIME) // "14:30:00"
    )

    // 2. 타입별 필드 처리 + 구분자(type) 추가
    when (mission) {
        is ExerciseMission -> {
            commonData["type"] = "EXERCISE"
            commonData["targetValue"] = mission.targetValue
            commonData["unit"] = mission.unit.name
            commonData["useSupportAgent"] = mission.useSupportAgent
            commonData["selectedExercise"] = mission.selectedExercise?.name
        }
        is DietMission -> {
            commonData["type"] = "DIET"
            commonData["recordMethod"] = mission.recordMethod.name
        }
        is RoutineMission -> {
            commonData["type"] = "ROUTINE"
            commonData["dailyTargetAmount"] = mission.dailyTargetAmount
            commonData["amountPerStep"] = mission.amountPerStep
            commonData["unitLabel"] = mission.unitLabel
        }
        is RestrictionMission -> {
            commonData["type"] = "RESTRICTION"
            commonData["restrictionType"] = mission.type.name // 변수명 type -> DB필드 restrictionType
            commonData["maxAllowedMinutes"] = mission.maxAllowedMinutes
        }
    }
    return commonData
}

// [조회용] Map -> Mission 객체 변환 (Type 기반으로 복구)
@RequiresApi(Build.VERSION_CODES.O)
fun mapToMission(id: String, data: Map<String, Any?>): Mission? {
    val type = data["type"] as? String ?: return null

    val title = data["title"] as? String ?: ""

    val daysList = data["days"] as? List<String> ?: emptyList()
    val days = daysList.mapNotNull { safeEnum<DayOfWeek>(it) }.toSet()

    val memo = data["memo"] as? String ?: null

    val timeString = data["notificationTime"] as? String
    val notificationTime = try {
        if (timeString != null) LocalTime.parse(timeString) else null
    } catch (e: Exception) {
        throw RuntimeException(e)
    }

    return when (type) {
        "EXERCISE" -> ExerciseMission(
            id = id,
            title = title,
            days = days,
            notificationTime = notificationTime,
            memo = memo,
            targetValue = (data["targetValue"] as? Number)?.toInt() ?: 0,
            unit = safeEnum<ExerciseRecordMode>(data["unit"] as? String) ?: ExerciseRecordMode.RUNNING,
            useSupportAgent = data["useSupportAgent"] as? Boolean ?: false,
            selectedExercise = safeEnum<AiExerciseType>(data["selectedExercise"] as? String)
        )
        "DIET" -> DietMission(
            id = id,
            title = title,
            days = days,
            notificationTime = notificationTime,
            memo = memo,
            recordMethod = safeEnum<DietRecordMethod>(data["recordMethod"] as? String) ?: DietRecordMethod.TEXT
        )
        "ROUTINE" -> RoutineMission(
            id = id,
            title = title,
            days = days,
            notificationTime = notificationTime,
            memo = memo,
            dailyTargetAmount = (data["dailyTargetAmount"] as? Number)?.toInt() ?: 0,
            amountPerStep = (data["amountPerStep"] as? Number)?.toInt() ?: 0,
            unitLabel = data["unitLabel"] as? String ?: ""
        )
        "RESTRICTION" -> RestrictionMission(
            id = id,
            title = title,
            days = days,
            notificationTime = notificationTime,
            memo = memo,
            type = safeEnum<RestrictionType>(data["restrictionType"] as? String) ?: RestrictionType.CHECK,
            maxAllowedMinutes = (data["maxAllowedMinutes"] as? Number)?.toInt()
        )
        else -> throw IllegalArgumentException(type)
    }
}

// Enum 변환 헬퍼 함수
private inline fun <reified T : Enum<T>> safeEnum(value: String?): T? {
    return try {
        if (value != null) enumValueOf<T>(value) else null
    } catch (e: Exception) {
        throw RuntimeException(e)

    }
}