package com.hye.shared.mock

import android.os.Build
import androidx.annotation.RequiresApi
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.model.mission.WeeklyMissionState
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RestrictionType
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.shared.util.getCurrentSeoulDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object MissionMockData {

    // 오늘 날짜 (예: "2024-02-14")
    // 오늘 날짜
    private val today = getCurrentSeoulDate()//LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMockMissions(): List<WeeklyMissionState> {

        // 🔥 재사용을 위한 공통 기록 객체 (오늘 완료한 건 주간 기록에도 포함되어야 하므로)
        val m3TodayRecord = MissionRecord(
            id = "r3", missionId = "m3", date = today, progress = 1, isCompleted = true,
            completedAt = LocalTime.of(12, 30).format(DateTimeFormatter.ISO_TIME)
        )
        val m4TodayRecord = MissionRecord(
            id = "r4", missionId = "m4", date = today, progress = 1, isCompleted = true,
            completedAt = LocalTime.of(23, 59).format(DateTimeFormatter.ISO_TIME)
        )

        return listOf(
            // 1. [운동] 스쿼트 (오늘은 아직 시작 안 함, 어제 1번 완료함 -> 주간 1/3회)
            WeeklyMissionState(
                mission = ExerciseMission(
                    id = "m1",
                    title = "매일 스쿼트 15회",
                    notificationTime = LocalTime.of(8, 0),
                    memo = "바른 자세로 하기",
                    targetValue = 15,
                    unit = ExerciseRecordMode.SELECTED,
                    selectedExercise = AiExerciseType.SQUAT,
                    useSupportAgent = true,
                    weeklyTargetCount = 3,
                    weekIdentifier = "2026-W10",
                    isTemplate = false
                ),
                todayRecord = null, // 🔥 record -> todayRecord 변경
                weeklyRecords = listOf( // 🔥 주간 기록 추가
                    MissionRecord(id = "r1_prev", missionId = "m1", date = today.minusDays(1), isCompleted = true)
                )
            ),

            // 2. [상시] 물 마시기 (오늘 50% 진행 중, 그제/어제 완료함 -> 주간 2/3회)
            WeeklyMissionState(
                mission = RoutineMission(
                    id = "m2",
                    title = "물 2L 마시기",
                    notificationTime = null,
                    dailyTargetAmount = 2000,
                    amountPerStep = 250,
                    unitLabel = "ml",
                    weeklyTargetCount = 3,
                    weekIdentifier = "2026-W10",
                    isTemplate = false,
                    memo = ""
                ),
                todayRecord = MissionRecord(
                    id = "r2",
                    missionId = "m2",
                    date = today,
                    progress = 1000, // 1000ml 마심
                    isCompleted = false
                ),
                weeklyRecords = listOf(
                    MissionRecord(id = "r2_prev1", missionId = "m2", date = today.minusDays(2), isCompleted = true),
                    MissionRecord(id = "r2_prev2", missionId = "m2", date = today.minusDays(1), isCompleted = true)
                )
            ),

            // 3. [식단] 점심 샐러드 (오늘 완료함 -> 주간 1/3회)
            WeeklyMissionState(
                mission = DietMission(
                    id = "m3",
                    title = "점심에 샐러드 먹기",
                    notificationTime = LocalTime.of(12, 0),
                    recordMethod = DietRecordMethod.PHOTO,
                    weeklyTargetCount = 3,
                    weekIdentifier = "2026-W10",
                    isTemplate = false,
                    memo = ""
                ),
                todayRecord = m3TodayRecord,
                weeklyRecords = listOf(m3TodayRecord) // 오늘 한 것도 이번 주 기록이니까 포함됨!
            ),

            // 4. [제한] 금연 (체크형, 오늘 완료함, 어제도 완료함 -> 주간 2/3회)
            WeeklyMissionState(
                mission = RestrictionMission(
                    id = "m4",
                    title = "하루 종일 금연",
                    notificationTime = LocalTime.of(22, 0),
                    type = RestrictionType.CHECK,
                    maxAllowedMinutes = null,
                    weeklyTargetCount = 3,
                    weekIdentifier = "2026-W10",
                    isTemplate = false,
                    memo = ""
                ),
                todayRecord = m4TodayRecord,
                weeklyRecords = listOf(
                    MissionRecord(id = "r4_prev", missionId = "m4", date = today.minusDays(1), isCompleted = true),
                    m4TodayRecord
                )
            ),

            // 5. [운동] 러닝 (AI 코칭 미사용, 오늘 10분 진행 중 -> 주간 0/3회)
            WeeklyMissionState(
                mission = ExerciseMission(
                    id = "m5",
                    title = "저녁 러닝 30분",
                    notificationTime = LocalTime.of(20, 0),
                    targetValue = 30,
                    unit = ExerciseRecordMode.RUNNING,
                    useSupportAgent = false,
                    weeklyTargetCount = 3,
                    weekIdentifier = "2026-W10",
                    isTemplate = false,
                    memo = ""
                ),
                todayRecord = MissionRecord(
                    id = "r5",
                    missionId = "m5",
                    date = today,
                    progress = 10, // 10분 뛰었음
                    isCompleted = false
                ),
                weeklyRecords = emptyList() // 이번 주에 완료한 적 없음
            )
        )
    }
}