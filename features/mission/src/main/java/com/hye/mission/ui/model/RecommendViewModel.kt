package com.hye.mission.ui.model

import androidx.lifecycle.viewModelScope
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.result.MissionResult
import com.hye.domain.session.SessionManager
import com.hye.domain.usecase.MissionUseCase
import com.hye.mission.ui.state.RecommendUiState
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val missionUseCases: MissionUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(RecommendUiState())
    val uiState = _uiState.asStateFlow()

    init {
        generateAgentRecommendations()
    }

    private fun generateAgentRecommendations() {
        viewModelScope.launch(commonCeh) {
            // 1. UI 상태: 로딩 시작
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    agentMessage = "요원님의 최근 신체 데이터 및 스캔 결과를 동기화 중입니다"
                )
            }

            // 2. 세션 매니저에서 가장 최신의 유저 프로필을 가져옴
            val profile = sessionManager.currentUser.value

            if (profile == null) {
                Timber.e("프로필 정보를 찾을 수 없습니다.")
                // UI 상태: 에러 발생
                _uiState.update { it.copy(isLoading = false, error = "프로필을 불러오지 못했습니다.") }
                return@launch
            }
            val loadingJob: Job = launch {
                delay(1500)
                _uiState.update {
                    it.copy(agentMessage = "지난주 작전 수행 이력을 바탕으로 취약점을 분석하고 있습니다")
                }

                delay(1500)
                _uiState.update {
                    it.copy(agentMessage = "분석 완료. 요원님을 위한 최적의 맞춤형 전술을 수립 중입니다")
                }
            }

            // 🔥 3. 하이브리드 AI 추천 엔진 가동
            val pipelineResult = missionUseCases.recommendMission(profile)

            loadingJob.cancel()

            // 4. UI 상태 업데이트: UseCase가 넘겨준 미션 리스트와 AI 브리핑 대사를 화면에 방출
            _uiState.update {
                it.copy(
                    isLoading = false,
                    recommendations = pipelineResult.recommendations,
                    agentMessage = pipelineResult.briefingMessage,

                    originalRecommendations = pipelineResult.recommendations,
                    originalAgentMessage = pipelineResult.briefingMessage,
                    isAdjusted = false
                )
            }
            Timber.d("에이전트 작전 수립 완료: ${pipelineResult.recommendations.size}개")
        }
    }

    fun acceptMission(mission: Mission) {
        viewModelScope.launch(commonCeh) {
            // 1. 화면의 추천 리스트에서 해당 미션 제거
            removeMissionFromList(mission.id)

            // 2. 데이터베이스에 미션 저장
            missionUseCases.insertMission(mission).collect { result ->
                when (result) {
                    is MissionResult.Success -> {
                        showToast("작전이 수락되었습니다. [${mission.title}]")
                        Timber.d("미션 저장 성공: ${mission.title}")
                    }
                    is MissionResult.Error -> {
                        showToast("작전 수락 중 오류가 발생했습니다.")
                        Timber.e(result.exception, "미션 저장 실패: ${result.exception.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    fun toggleAdjustmentMode() {
        _uiState.update { it.copy(isAdjustmentMode = !it.isAdjustmentMode) }
    }

    fun updateAdjustmentInput(text: String) {
        _uiState.update { it.copy(adjustmentInput = text) }
    }

    fun requestAdjustment() {
        val feedback = _uiState.value.adjustmentInput
        if (feedback.isBlank()) return // 빈칸이면 무시

        viewModelScope.launch(commonCeh) {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isAdjustmentMode = false, // 전송했으니 입력창은 닫음
                    agentMessage = "요원님의 요청 사항을 반영하여 작전을 재검토 중입니다..."
                )
            }

            val profile = sessionManager.currentUser.value ?: return@launch

            val pipelineResult = missionUseCases.recommendMission(profile, feedback)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    recommendations = pipelineResult.recommendations,
                    agentMessage = pipelineResult.briefingMessage,
                    isAdjusted = true,   // 조정 완료 상태로 변경 ('되돌리기' 버튼 활성화)
                    adjustmentInput = "" // 입력창 비우기
                )
            }
        }
    }

    fun revertToOriginal() {
        _uiState.update {
            it.copy(
                recommendations = it.originalRecommendations,
                agentMessage = it.originalAgentMessage,
                isAdjusted = false,
                isAdjustmentMode = false,
                adjustmentInput = ""
            )
        }
        showToast("원래의 작전으로 복구되었습니다.")
    }


    // 작전 보류(거절) 시 로직
    fun rejectMission(mission: Mission) {
        removeMissionFromList(mission.id)
        showToast("해당 작전을 보류합니다.")
    }

    private fun removeMissionFromList(missionId: String) {
        _uiState.update { state ->
            state.copy(
                recommendations = state.recommendations.filterNot { it.mission.id == missionId }
            )
        }
    }
}