package com.hye.shared.mock

import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.model.mission.MissionWithRecord
import com.hye.domain.model.mission.types.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object MissionMockData {

    // 오늘 날짜 (예: "2024-02-14")
    private val today = "2024-02-14"

    fun getMockMissions(): List<MissionWithRecord> {
        return listOf(
            // 1. [운동] 스쿼트 (아직 시작 안 함)
            MissionWithRecord(
                mission = ExerciseMission(
                    id = "m1",
                    title = "매일 스쿼트 15회",
                    days = setOf(DayOfWeek.MON, DayOfWeek.WED, DayOfWeek.FRI),
                    notificationTime = LocalTime.of(8, 0),
                    memo = "바른 자세로 하기",
                    targetValue = 15,
                    unit = ExerciseRecordMode.SELECTED,
                    selectedExercise = AiExerciseType.SQUAT.label,
                    useSupportAgent = true
                ),
                record = null // 기록 없음
            ),

            // 2. [상시] 물 마시기 (진행 중 - 50%)
            MissionWithRecord(
                mission = RoutineMission(
                    id = "m2",
                    title = "물 2L 마시기",
                    days = DayOfWeek.values().toSet(), // 매일
                    notificationTime = null,
                    dailyTargetAmount = 2000,
                    amountPerStep = 250,
                    unitLabel = "ml"
                ),
                record = MissionRecord(
                    id = "r2",
                    missionId = "m2",
                    date = today,
                    progress = 1000, // 1000ml 마심
                    isCompleted = false
                )
            ),

            // 3. [식단] 점심 샐러드 (완료됨)
            MissionWithRecord(
                mission = DietMission(
                    id = "m3",
                    title = "점심에 샐러드 먹기",
                    days = setOf(DayOfWeek.MON, DayOfWeek.TUE, DayOfWeek.WED, DayOfWeek.THU, DayOfWeek.FRI),
                    notificationTime = LocalTime.of(12, 0),
                    recordMethod = DietRecordMethod.PHOTO
                ),
                record = MissionRecord(
                    id = "r3",
                    missionId = "m3",
                    date = today,
                    progress = 1,
                    isCompleted = true, // 완료!
                    completedAt = LocalTime.of(12, 30).format(DateTimeFormatter.ISO_TIME)
                )
            ),

            // 4. [제한] 금연 (체크형, 완료됨)
            MissionWithRecord(
                mission = RestrictionMission(
                    id = "m4",
                    title = "하루 종일 금연",
                    days = DayOfWeek.values().toSet(),
                    notificationTime = LocalTime.of(22, 0),
                    type = RestrictionType.CHECK,
                    maxAllowedMinutes = null
                ),
                record = MissionRecord(
                    id = "r4",
                    missionId = "m4",
                    date = today,
                    progress = 1,
                    isCompleted = true,
                    completedAt = LocalTime.of(23, 59).format(DateTimeFormatter.ISO_TIME)
                )
            ),

            // 5. [운동] 러닝 (AI 코칭 미사용, 진행 중)
            MissionWithRecord(
                mission = ExerciseMission(
                    id = "m5",
                    title = "저녁 러닝 30분",
                    days = setOf(DayOfWeek.TUE, DayOfWeek.THU, DayOfWeek.SAT),
                    notificationTime = LocalTime.of(20, 0),
                    targetValue = 30,
                    unit = ExerciseRecordMode.RUNNING,
                    useSupportAgent = false
                ),
                record = MissionRecord(
                    id = "r5",
                    missionId = "m5",
                    date = today,
                    progress = 10, // 10분 뛰었음
                    isCompleted = false
                )
            )
        )
    }
}