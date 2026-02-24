package com.hye.mission.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.hye.domain.factory.MissionFactory
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.DayOfWeek
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.types.MissionType
import com.hye.domain.model.mission.types.RestrictionType
import com.hye.domain.result.MissionResult
import com.hye.domain.usecase.MissionUseCase
import com.hye.mission.ui.state.MissionState
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MissionCreationViewModel @Inject constructor(
    private val missionUseCase: MissionUseCase
): BaseViewModel(){

    private val _uiStatus = MutableStateFlow(MissionState())
    val uiStatus = _uiStatus.asStateFlow()

    init {
        startCreation()
    }

    fun toggleBottomSheet(isOpen: Boolean) {
        _uiStatus.update { it.copy(isBottomSheetOpen = isOpen) }
    }


    fun insertMission() = viewModelScope.launch(commonCeh) {
        val state = _uiStatus.value

        if (state.titleInput.isBlank()) {
            showToast("미션 제목을 입력해주세요.")
            return@launch
        }

        val commonData = MissionFactory.MissionData(
            title = state.titleInput,
            days = state.daysInput,
            memo = state.memoInput.ifBlank { null },
            notificationTime = state.notificationTimeInput
        )

        val finalMission: Mission = try {
            when (state.selectedCategory) {
                MissionType.EXERCISE -> {
                    // 특화 유효성 검사
                    requireNotNull(state.exerciseTargetInput) { "목표 수치를 입력해주세요." }

                    MissionFactory.createExerciseMission(
                        data = commonData,
                        unit = state.exerciseUnitInput,
                        targetValue = state.exerciseTargetInput,
                        useSupportAgent = state.useSupportAgentInput,
                        selectedExercise = state.selectedExerciseTypeInput
                    )
                }
                MissionType.DIET -> {
                    MissionFactory.createDietMission(
                        data = commonData,
                        recordMethod = state.dietRecordMethodInput
                    )
                }
                MissionType.ROUTINE -> {
                    requireNotNull(state.routineDailyTargetInput) { "하루 목표량을 입력해주세요." }
                    requireNotNull(state.routineStepAmountInput) { "1회 증가량을 입력해주세요." }

                    MissionFactory.createRoutineMission(
                        data = commonData,
                        dailyTargetAmount = state.routineDailyTargetInput,
                        amountPerStep = state.routineStepAmountInput,
                        unitLabel = state.routineUnitLabelInput
                    )
                }
                MissionType.RESTRICTION -> {
                    MissionFactory.createRestrictionMission(
                        data = commonData,
                        type = state.restrictionTypeInput,
                        maxAllowedMinutes = state.restrictionMaxMinutesInput
                    )
                }
            }
        } catch (e: IllegalArgumentException) {
            showToast(e.message ?: "입력값을 다시 확인해주세요.")
            return@launch
        }

        Timber.d("Starting mission insertion: ${finalMission.title}")
        _uiStatus.update { it.copy(isLoading = true) }

        missionUseCase.insertMission(finalMission).collect { result ->
            when (result) {
                is MissionResult.Loading -> _uiStatus.update { it.copy(isLoading = true) }
                is MissionResult.Success -> {
                    Timber.i("Mission saved successfully: ${result.resultData}")
                    val msg = result.resultData.result ?: "미션 저장이 완료 되었습니다"
                    showToast(msg)
                    startCreation(state.selectedCategory)
                }
                is MissionResult.Error -> {
                    _uiStatus.update { it.copy(isLoading = false) }
                    val msg = result.exception.message ?: "저장에 실패하였습니다"
                    showToast(msg)
                }
                else -> _uiStatus.update { it.copy(isLoading = false) }
            }
        }
    }
    //-------------------
    // 초기화 (카테고리 선택)
    fun startCreation(category: MissionType = MissionType.EXERCISE) {
        _uiStatus.update {
            MissionState(selectedCategory = category)
        }
    }

    fun changeCategory(category: MissionType) {
        _uiStatus.update { it.copy(selectedCategory = category) }
    }

    //-------------------
    // 공통 필드 업데이트
    fun updateTitle(newTitle: String) {
        _uiStatus.update { it.copy(titleInput = newTitle) }
    }

    fun toggleDay(day: DayOfWeek) {
        _uiStatus.update { state ->
            val newDays = if (state.daysInput.contains(day)) {
                state.daysInput - day
            } else {
                state.daysInput + day
            }
            state.copy(daysInput = newDays)
        }
    }

    fun updateMemo(newMemo: String) {
        _uiStatus.update { it.copy(memoInput = newMemo) }
    }
    //-------------------
    // 카테고리별 필드 업데이트
    // T : Mission -> 특정 미션 타입(예: ExerciseMission)인지 확인하고, 맞으면 block을 실행
    // [운동]
    fun selectExerciseType(type: AiExerciseType) {
        _uiStatus.update { state ->
            state.copy(
                selectedExerciseTypeInput = type,
                exerciseTargetInput = type.defaultTarget,
                isBottomSheetOpen = false
            )
        }
    }
    fun updateExerciseUnit(unit: ExerciseRecordMode) {
        _uiStatus.update { it.copy(exerciseUnitInput = unit) }
    }
    fun updateExerciseTarget(targetStr: String) {
        _uiStatus.update { it.copy(exerciseTargetInput = targetStr.toIntOrDefault()) }
    }
    fun toggleExerciseSupportAgent(useSupportAgent: Boolean) {
        _uiStatus.update { it.copy(useSupportAgentInput = useSupportAgent) }
    }
    // [식단]
    fun updateDietRecordMethod(method: DietRecordMethod) {
        _uiStatus.update { it.copy(dietRecordMethodInput = method) }
    }

    // [상시]
    fun updateRoutineUnitLabel(label: String) {
        _uiStatus.update { it.copy(routineUnitLabelInput = label) }
    }
    fun updateRoutineTotalTarget(targetStr: String) {
        _uiStatus.update { it.copy(routineDailyTargetInput = targetStr.toIntOrDefault()) }
    }
    fun updateRoutineStepAmount(amountStr: String) {
        _uiStatus.update { it.copy(routineStepAmountInput = amountStr.toIntOrDefault()) }
    }
    // [제한]
    fun updateRestrictionType(type: RestrictionType) {
        _uiStatus.update { it.copy(restrictionTypeInput = type) }
    }
    fun updateRestrictionTime(timeStr: String) {
        // 시간 단위를 분으로 환산 (예: 2시간 -> 120분)
        _uiStatus.update { it.copy(restrictionMaxMinutesInput = (timeStr.toIntOrDefault())?.times(60)) }
    }

    private fun String.toIntOrDefault(default: Int? = null): Int? =
        this.filter { it.isDigit() }.toIntOrNull() ?: default
}