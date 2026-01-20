package com.hye.mission.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.model.mission.DayOfWeek
import com.hye.domain.model.mission.DietMission
import com.hye.domain.model.mission.DietRecordMethod
import com.hye.domain.model.mission.ExerciseMission
import com.hye.domain.model.mission.ExerciseUnit
import com.hye.domain.model.mission.Mission
import com.hye.domain.model.mission.MissionCategory
import com.hye.domain.model.mission.RestrictionMission
import com.hye.domain.model.mission.RestrictionType
import com.hye.domain.model.mission.RoutineMission
import com.hye.domain.model.mission.copyCommon
import com.hye.domain.result.MissionResult
import com.hye.domain.usecase.mission.MissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MissionCreationViewModel @Inject constructor(
    private val missionUseCase: MissionUseCase
): ViewModel(){
    private val _uiStatus = MutableStateFlow(MissionState())
    val uiStatus = _uiStatus.asStateFlow()

    init {
        startCreation()
    }

    // 저장
    fun insertMission() = viewModelScope.launch {
        val inputMission = _uiStatus.value.inputMission ?: return@launch

        missionUseCase.insertMission(inputMission).collect { result ->
            _uiStatus.update { currentState ->
                when (result) {
                    is MissionResult.Loading -> currentState.copy(isLoading = true)
                    is MissionResult.Success -> {
                        currentState.copy(
                            isLoading = false,
                            isInserted = true,
                            userMessage = result.resultData.result // "성공했습니다" 메시지
                        )
                    }
                    is MissionResult.FirebaseError -> {
                        currentState.copy(
                            isLoading = false,
                            userMessage = result.exception.message ?: "저장 실패"
                        )
                    }
                    else -> currentState.copy(isLoading = false)
                }
            }
        }
    }
    fun resetMissionState(){
        _uiStatus.update { MissionState() }
        startCreation()
    }
    private fun updateMissionState(mission: Mission) {
        _uiStatus.update { it.copy(inputMission = mission) }
    }

    //-------------------
    // 초기화 (카테고리 선택)
    fun startCreation(category: MissionCategory = MissionCategory.EXERCISE) {
        val current = _uiStatus.value.inputMission
        val id = current?.id ?: UUID.randomUUID().toString()
        val title = current?.title ?: ""
        val days = current?.days ?: emptySet()
        val tags = current?.tags ?: emptyList()
        val notificationTime = current?.notificationTime

        val newMission = when (category) {
            MissionCategory.EXERCISE -> ExerciseMission(
                id = id, title = title, days = days, tags = tags, notificationTime = notificationTime,
                targetValue = 0, unit = ExerciseUnit.TIME, useTimer = false
            )
            MissionCategory.DIET -> DietMission(
                id = id, title = title, days = days, tags = tags, notificationTime = notificationTime,
                recordMethod = DietRecordMethod.TEXT
            )
            MissionCategory.ROUTINE -> RoutineMission(
                id = id, title = title, days = days, tags = tags, notificationTime = notificationTime,
                dailyTargetAmount = 0, amountPerStep = 0, unitLabel = ""
            )
            MissionCategory.RESTRICTION -> RestrictionMission(
                id = id, title = title, days = days, tags = tags, notificationTime = notificationTime,
                type = RestrictionType.CHECK, maxAllowedMinutes = null
            )
        }
        updateMissionState(newMission)
    }
    //-------------------
    // 공통 필드 업데이트
    private inline fun updateCommon(block: (Mission) -> Mission) {
        val current = _uiStatus.value.inputMission ?: return
        val updated = block(current)
        updateMissionState(updated)
    }

    fun updateTitle(newTitle: String) = updateCommon {
        it.copyCommon(title = newTitle)
    }

    fun toggleDay(day: DayOfWeek) = updateCommon {
        val newDays = if (it.days.contains(day)) it.days - day else it.days + day
        it.copyCommon(days = newDays)
    }

    fun addTag(tag: String) = updateCommon {
        if (tag.isNotBlank() && !it.tags.contains(tag)) {
            it.copyCommon(tags = it.tags + tag.trim())
        } else it
    }

    fun removeTag(tag: String) = updateCommon {
        it.copyCommon(tags = it.tags - tag)
    }
    //-------------------
    // 카테고리별 필드 업데이트

    // T : Mission -> 특정 미션 타입(예: ExerciseMission)인지 확인하고, 맞으면 block을 실행
    private inline fun <reified T : Mission> updateIfIs(block: (T) -> Mission) {
        val current = _uiStatus.value.inputMission
        if (current is T) {
            updateMissionState(block(current))
        }
    }
    // [운동]
    fun updateExerciseUnit(unit: ExerciseUnit) = updateIfIs<ExerciseMission> {
        it.copy(unit = unit)
    }
    fun updateExerciseTarget(targetStr: String) = updateIfIs<ExerciseMission> {
        val value = targetStr.filter { c -> c.isDigit() }.toIntOrNull() ?: 0
        it.copy(targetValue = value)
    }
    fun toggleExerciseTimer(useTimer: Boolean) = updateIfIs<ExerciseMission> {
        it.copy(useTimer = useTimer)
    }

    // [식단]
    fun updateDietRecordMethod(method: DietRecordMethod) = updateIfIs<DietMission> {
        it.copy(recordMethod = method)
    }

    // [상시]
    fun updateRoutineUnitLabel(label: String) = updateIfIs<RoutineMission> {
        it.copy(unitLabel = label)
    }
    fun updateRoutineTotalTarget(targetStr: String) = updateIfIs<RoutineMission> {
        val value = targetStr.filter { c -> c.isDigit() }.toIntOrNull() ?: 0
        it.copy(dailyTargetAmount = value)
    }
    fun updateRoutineStepAmount(amountStr: String) = updateIfIs<RoutineMission> {
        val value = amountStr.filter { c -> c.isDigit() }.toIntOrNull() ?: 0
        it.copy(amountPerStep = value)
    }

    // [제한]
    fun updateRestrictionType(type: RestrictionType) = updateIfIs<RestrictionMission> {
        it.copy(type = type)
    }
    fun updateRestrictionTime(timeStr: String) = updateIfIs<RestrictionMission> {
        val hours = timeStr.filter { c -> c.isDigit() }.toIntOrNull() ?: 0
        it.copy(maxAllowedMinutes = hours * 60)
    }


}