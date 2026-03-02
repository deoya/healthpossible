package com.hye.mission.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.model.mission.WeeklyMissionState
import com.hye.domain.model.mission.types.DietMission
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.RestrictionMission
import com.hye.domain.model.mission.types.RoutineMission
import com.hye.domain.result.MissionResult
import com.hye.domain.usecase.MissionUseCase
import com.hye.mission.ui.state.MissionState
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MissionViewModel @Inject constructor(
    private val missionUseCase: MissionUseCase
) : BaseViewModel() {

    private val _uiStatus = MutableStateFlow(MissionState())
    val uiStatus = _uiStatus.asStateFlow()

    private val todayDate = LocalDate.now()

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch (commonCeh){
        val missionsFlow = missionUseCase.getMissionList()
        val recordsFlow = missionUseCase.getMissionRecords(todayDate)
        combine(missionsFlow, recordsFlow) { missionsResult, recordsResult ->
            if (missionsResult is MissionResult.Success && recordsResult is MissionResult.Success) {
                val missions = missionsResult.resultData
                val records = recordsResult.resultData
                val recordsByMission = records.groupBy { it.missionId }

                val mergedList = missions.map { mission ->
                    val missionRecords = recordsByMission[mission.id] ?: emptyList()
                    val todayRecord = missionRecords.find { it.date == todayDate }

                    WeeklyMissionState(
                        mission = mission,
                        todayRecord = todayRecord,
                        weeklyRecords = missionRecords
                    )
                }
                MissionResult.Success(mergedList)

            } else if (missionsResult is MissionResult.Loading || recordsResult is MissionResult.Loading) {
                MissionResult.Loading
            } else {
                val exception = (missionsResult as? MissionResult.Error)?.exception
                    ?: (recordsResult as? MissionResult.Error)?.exception
                    ?: Exception("데이터 로드 실패")
                Timber.e(exception, "Failed to load mission data")
                MissionResult.Error(exception)
            }
        }.collectLatest { combinedResult ->
            updateUiState(combinedResult)
        }
    }

    private fun updateUiState(result: MissionResult<List<WeeklyMissionState>>) {

        when (result) {
            is MissionResult.Loading -> _uiStatus.update { it.copy(isLoading = true, errorMessage = null) }
            is MissionResult.Success -> {
                val data = result.resultData
                Timber.i("Mission data loaded: ${data.size} items")
                _uiStatus.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        missions = data,
                        totalMissionsCount = data.size,
                        completedMissionsCount = data.count { item -> item.isDoneToday }
                    )
                }
            }
            is MissionResult.Error -> {
                _uiStatus.update {
                    it.copy(isLoading = false, errorMessage = result.exception.message, missions = emptyList())
                }
            }
            is MissionResult.NoConstructor -> {}
        }
    }

    fun onStartButtonClicked(item: WeeklyMissionState) {
        val mission = item.mission
        val recordId = "${mission.id}_$todayDate"

        Timber.d("Start button clicked: ${mission.title}")

        // 🔥 item.record 대신 item.todayRecord 사용
        val currentRecord = item.todayRecord ?: MissionRecord(
            id = recordId,
            missionId = mission.id,
            date = todayDate
        )

        if (currentRecord.isCompleted) {
            viewModelScope.launch {
                showToast("이미 완료된 미션입니다! 🎉")
            }
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
        viewModelScope.launch (commonCeh){
            updateLocalState(record)

            missionUseCase.updateMissionRecord(record).collect { result ->
                when(result){
                    is MissionResult.Success -> {
                        if (record.isCompleted) {
                            Timber.i("Mission completed: ${record.missionId}") // ✅ 완료 로그
                            showToast("미션 성공! 저장되었습니다.")
                        }
                    }
                    is MissionResult.Error -> {
                        Timber.w(result.exception, "Save failed for record: ${record.id}") // ✅ 경고 로그
                        showToast("저장 실패: ${result.exception.message}")

                    }
                    else -> {}
                }
            }
        }
    }

    private fun updateLocalState(updatedRecord: MissionRecord) {
        _uiStatus.update { state ->
            val newMissions = state.missions.map { item ->
                if (item.mission.id == updatedRecord.missionId) {

                    val newWeeklyRecords = item.weeklyRecords.filterNot { it.id == updatedRecord.id } + updatedRecord
                    item.copy(
                        todayRecord = updatedRecord,
                        weeklyRecords = newWeeklyRecords
                    )
                } else {
                    item
                }
            }
            state.copy(
                missions = newMissions,
                completedMissionsCount = newMissions.count { it.isDoneToday }
            )
        }
    }
}