package com.hye.mission.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.AiSessionMode
import com.hye.domain.model.mission.types.ExerciseAgentType
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.mission.ui.state.RecordState
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MissionRecordingViewModel @Inject constructor(

): BaseViewModel() {
    private val _uiState = MutableStateFlow(RecordState())
    val uiState: StateFlow<RecordState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun initSession(mission: ExerciseMission) {
        viewModelScope.launch(commonCeh) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            Timber.d("Initializing session for mission: ${mission.title}")

            try {
                // A. 에이전트 타입 설정
                val agentType = if (mission.unit == ExerciseRecordMode.RUNNING) {
                    ExerciseAgentType.RUNNING
                } else {
                    ExerciseAgentType.AI_POSTURE
                }

                // B. 세션 모드 초기화 로직
                val initialMode = when (mission.unit) {
                    ExerciseRecordMode.RUNNING -> {
                        AiSessionMode.DurationMode(
                            targetSeconds = mission.targetValue * 60
                        )
                    }
                    ExerciseRecordMode.SELECTED -> {
                        val type = AiExerciseType.values().find { it.name == mission.selectedExercise }
                            ?: AiExerciseType.SQUAT

                        AiSessionMode.AiRepMode(
                            exerciseType = type,
                            targetCount = type.defaultTarget
                        )
                    }
                }

                // 초기화 성공 상태 업데이트
                _uiState.update {
                    it.copy(
                        isLoading = false, // 로딩 끝
                        exerciseAgentType = agentType,
                        sessionMode = initialMode
                    )
                }

                // 세션 시작과 동시에 타이머 가동
                startTimer()

            } catch (e: Exception) {
                // 초기화 중 로직 에러 발생 시 처리
                Timber.e(e, "Session initialization failed")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "세션을 초기화하는 중 문제가 발생했습니다."
                    )
                }
            }
        }
    }

    fun selectExercise(type: AiExerciseType) {
        _uiState.update { state ->
            val mode = state.sessionMode
            if (mode is AiSessionMode.AiRepMode) {
                Timber.i("Exercise changed to: ${type.name}") // 로그
                state.copy(
                    sessionMode = mode.copy(
                        exerciseType = type,
                        targetCount = type.defaultTarget,
                        currentCount = 0
                    ),
                    isBottomSheetOpen = false
                )
            } else {
                state
            }
        }
    }

    fun toggleBottomSheet(isOpen: Boolean) {
        _uiState.update { it.copy(isBottomSheetOpen = isOpen) }
    }

    fun increaseCount() {
        _uiState.update { state ->
            when (val mode = state.sessionMode) {
                is AiSessionMode.AiRepMode -> {
                    if (mode.currentCount + 1 == mode.targetCount) showToast("목표 달성! 🔥")
                    state.copy(sessionMode = mode.copy(currentCount = mode.currentCount + 1))
                }
                is AiSessionMode.DurationMode -> state
            }
        }
    }

    fun startTimer() {
        if (timerJob?.isActive == true) return

        Timber.d("Timer started") // 로그
        _uiState.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _uiState.update { state ->
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

    fun pauseTimer() {
        Timber.d("Timer paused") // 로그
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }

    fun toggleTimer() {
        if (_uiState.value.isRunning) pauseTimer() else startTimer()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        Timber.d("ViewModel cleared, timer cancelled")
    }

    fun updateFeedback(message: String) {
        // 메시지가 같으면 업데이트 안 함 (불필요한 리컴포지션 방지)
        if (_uiState.value.feedbackMessage == message) return

        _uiState.update {
            it.copy(feedbackMessage = message)
        }
    }
}