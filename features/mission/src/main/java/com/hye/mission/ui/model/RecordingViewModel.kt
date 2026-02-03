package com.hye.mission.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.AiSessionMode
import com.hye.domain.model.mission.types.ExerciseAgentType
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseRecordMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MissionRecordingViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow(RecordState())
    val uiState: StateFlow<RecordState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    // 1. 세션 초기화 (화면 진입 시 호출)
    fun initSession(mission: ExerciseMission) {
        // A. 에이전트 타입 설정 (러닝화면 vs 카메라화면)
        val agentType = if (mission.unit == ExerciseRecordMode.RUNNING) {
            ExerciseAgentType.RUNNING
        } else {
            ExerciseAgentType.AI_POSTURE
        }

        // B. 세션 모드 초기화
        val initialMode = when (mission.unit) {
            // Case 1: 러닝 (시간 중심)
            ExerciseRecordMode.RUNNING -> {
                AiSessionMode.DurationMode(
                    targetSeconds = mission.targetValue * 60
                )
            }

            // Case 2: 종목 선택 (AI 카운팅 중심)
            ExerciseRecordMode.SELECTED -> {
                // 저장된 종목 이름으로 Enum 찾기 (없으면 기본값 스쿼트)
                val type = AiExerciseType.values().find { it.name == mission.selectedExercise }
                    ?: AiExerciseType.SQUAT

                AiSessionMode.AiRepMode(
                    exerciseType = type,
                    targetCount = type.defaultTarget // Enum에 정의된 목표치 사용
                )
            }
        }

        _uiState.update {
            it.copy(
                exerciseAgentType = agentType,
                sessionMode = initialMode
            )
        }

        startTimer()
    }

    // 2. 운동 종목 변경 (SELECTED 모드일 때만 동작)
    fun selectExercise(type: AiExerciseType) {
        _uiState.update { state ->
            val mode = state.sessionMode
            if (mode is AiSessionMode.AiRepMode) {
                state.copy(
                    sessionMode = mode.copy(
                        exerciseType = type,
                        targetCount = type.defaultTarget,
                        currentCount = 0 // 종목 바뀌면 카운트 리셋
                    ),
                    isBottomSheetOpen = false
                )
            } else {
                state // 러닝 모드에서는 변경 불가
            }
        }
    }

    // 3. 바텀시트 열기/닫기
    fun toggleBottomSheet(isOpen: Boolean) {
        _uiState.update { it.copy(isBottomSheetOpen = isOpen) }
    }

    fun increaseCount() {
        _uiState.update { state ->
            when(val mode = state.sessionMode) {
                is AiSessionMode.AiRepMode -> {
                     state.copy(sessionMode = mode.copy(currentCount = mode.currentCount + 1))
                }
                is AiSessionMode.DurationMode -> state
            }
        }
    }

    fun startTimer() {
        if (timerJob?.isActive == true) return // 이미 돌고 있으면 패스

        _uiState.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L) // 1초 대기
                _uiState.update { state ->
                    // 현재 모드가 DurationMode(러닝)일 때만 시간 증가
                    val mode = state.sessionMode
                    if (mode is AiSessionMode.DurationMode) {
                        state.copy(
                            sessionMode = mode.copy(
                                currentSeconds = mode.currentSeconds + 1
                            )
                        )
                    } else state
                }
            }
        }
    }

    // 3. 타이머 일시정지 (Pause)
    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }

    // 4. 타이머 토글 (UI 버튼 연결용)
    fun toggleTimer() {
        if (_uiState.value.isRunning) pauseTimer() else startTimer()
    }

    // 화면 나갈 때 타이머 정리
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}