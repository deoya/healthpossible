package com.hye.mission.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.model.mission.MissionWithRecord
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.domain.result.MissionResult
import com.hye.domain.usecase.mission.MissionUseCase
import com.hye.shared.util.getCurrentFormattedTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MissionViewModel @Inject constructor(
    private val missionUseCase: MissionUseCase
) : ViewModel() {

    private val _uiStatus = MutableStateFlow(MissionState())
    val uiStatus = _uiStatus.asStateFlow()

    private val todayDate = getCurrentFormattedTime()

    init {
        loadData()
    }

    private fun loadData() {
        val missionsFlow = missionUseCase.getMissionList()
        val recordsFlow = missionUseCase.getMissionRecords(todayDate)

        viewModelScope.launch {
            combine(missionsFlow, recordsFlow) { missionsResult, recordsResult ->
                if (missionsResult is MissionResult.Success && recordsResult is MissionResult.Success) {
                    val missions = missionsResult.resultData
                    val records = recordsResult.resultData
                    val recordsMap = records.associateBy { it.missionId }

                    val mergedList = missions.map { mission ->
                        MissionWithRecord(mission = mission, record = recordsMap[mission.id])
                    }
                    MissionResult.Success(mergedList)
                } else if (missionsResult is MissionResult.Loading || recordsResult is MissionResult.Loading) {
                    MissionResult.Loading
                } else {
                    val exception = (missionsResult as? MissionResult.FirebaseError)?.exception
                        ?: (recordsResult as? MissionResult.FirebaseError)?.exception
                        ?: Exception("데이터 로드 실패")
                    MissionResult.FirebaseError(exception)
                }
            }.collectLatest { combinedResult ->
                updateUiState(combinedResult)
            }
        }
    }

    private fun updateUiState(result: MissionResult<List<MissionWithRecord>>) {
        when (result) {
            is MissionResult.Loading -> _uiStatus.update { it.copy(isLoading = true, errorMessage = null) }
            is MissionResult.Success -> {
                val data = result.resultData
                _uiStatus.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        missions = data,
                        totalMissionsCount = data.size,
                        completedMissionsCount = data.count { item -> item.record?.isCompleted == true }
                    )
                }
            }
            is MissionResult.FirebaseError -> {
                _uiStatus.update {
                    it.copy(isLoading = false, errorMessage = result.exception.message, missions = emptyList())
                }
            }
            else -> {}
        }
    }

    fun onStartButtonClicked(item: MissionWithRecord) {
        val mission = item.mission
        val recordId = "${mission.id}_$todayDate"

        val currentRecord = item.record ?: MissionRecord(
            id = recordId,
            missionId = mission.id,
            date = todayDate
        )

        if (currentRecord.isCompleted) {
            _uiStatus.update { it.copy(userMessage = "이미 완료된 미션입니다! 🎉") }
            return
        }

        // 미션 타입별 로직
        // Todo : 실제 각 타입에 맞는 로직 추가 예정
        val nextRecord = when (mission) {
            is RoutineMission -> {
                val nextProgress = currentRecord.progress + mission.amountPerStep
                val isFinished = nextProgress >= mission.dailyTargetAmount
                val finalProgress = if (isFinished) mission.dailyTargetAmount else nextProgress
                currentRecord.copy(progress = finalProgress, isCompleted = isFinished)
            }
            is ExerciseMission -> {
                val nextProgress = currentRecord.progress + 1
                val isFinished = nextProgress >= mission.targetValue
                currentRecord.copy(progress = nextProgress, isCompleted = isFinished)
            }
            is DietMission, is RestrictionMission -> {
                currentRecord.copy(isCompleted = true, progress = 1)
            }
        }

        saveRecord(nextRecord)
    }

    private fun saveRecord(record: MissionRecord) {
        viewModelScope.launch {
            updateLocalState(record)

            missionUseCase.updateMissionRecord(record).collect { result ->
                if (result is MissionResult.FirebaseError) {
                    _uiStatus.update { it.copy(userMessage = "저장 실패: ${result.exception.message}") }
                } else if (result is MissionResult.Success) {
                    if (record.isCompleted) {
                        _uiStatus.update { it.copy(userMessage = "미션 성공! 저장되었습니다.") }
                    }
                }
            }
        }
    }

    private fun updateLocalState(updatedRecord: MissionRecord) {
        _uiStatus.update { state ->
            val newMissions = state.missions.map { item ->
                if (item.mission.id == updatedRecord.missionId) item.copy(record = updatedRecord) else item
            }
            state.copy(
                missions = newMissions,
                completedMissionsCount = newMissions.count { it.record?.isCompleted == true }
            )
        }
    }

    fun onMessageShown() {
        _uiStatus.update { it.copy(userMessage = null) }
    }
}