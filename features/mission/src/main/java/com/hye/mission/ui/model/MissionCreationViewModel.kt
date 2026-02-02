package com.hye.mission.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.factory.MissionFactory
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
import com.hye.domain.model.mission.types.copyCommon
import com.hye.domain.result.MissionResult
import com.hye.domain.usecase.mission.MissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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


    fun toggleBottomSheet(isOpen: Boolean) {
        _uiStatus.update { it.copy(isBottomSheetOpen = isOpen) }
    }

    fun selectExerciseType(typeLabel: String) {
        _uiStatus.update { state ->
            val nextState = state.copy(
                selectedExerciseType = typeLabel,
                isBottomSheetOpen = false
            )
            when(state.inputMission){
                is ExerciseMission -> {
                    val updatedMission = state.inputMission.copy(
                        selectedExercise = typeLabel
                    )
                    nextState.copy(inputMission = updatedMission)
                }
                else -> nextState
            }
        }
    }

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
    fun startCreation(category: MissionType = MissionType.EXERCISE) {
        val current = _uiStatus.value.inputMission
        val id = current?.id ?: UUID.randomUUID().toString()
        val title = current?.title ?: ""
        val days = current?.days ?: emptySet()
        val memo = current?.memo ?: null
        val notificationTime = current?.notificationTime

        val newMission = MissionFactory.create(id, category, title, days, memo, notificationTime)
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

    fun updateMemo(newMemo: String) = updateCommon {
        it.copyCommon(memo = newMemo)
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
    fun updateExerciseUnit(unit: ExerciseRecordMode) = updateIfIs<ExerciseMission> {
        it.copy(unit = unit)
    }
    fun updateExerciseTarget(targetStr: String) = updateIfIs<ExerciseMission> {
        it.copy(targetValue = targetStr.toIntOrDefault())
    }
    fun toggleExerciseSupportAgent(useSupportAgent: Boolean) = updateIfIs<ExerciseMission> {
        it.copy(useSupportAgent = useSupportAgent)
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
        it.copy(dailyTargetAmount = targetStr.toIntOrDefault())
    }
    fun updateRoutineStepAmount(amountStr: String) = updateIfIs<RoutineMission> {
        it.copy(amountPerStep = amountStr.toIntOrDefault())
    }
    // [제한]
    fun updateRestrictionType(type: RestrictionType) = updateIfIs<RestrictionMission> {
        it.copy(type = type)
    }
    fun updateRestrictionTime(timeStr: String) = updateIfIs<RestrictionMission> {
        it.copy(maxAllowedMinutes = (timeStr.toIntOrDefault()) * 60)
    }
    private fun String.toIntOrDefault(default: Int = 0) = this.filter { it.isDigit() }.toIntOrNull() ?: default
}