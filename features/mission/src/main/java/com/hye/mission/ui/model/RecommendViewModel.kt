package com.hye.mission.ui.model

import androidx.lifecycle.viewModelScope
import com.hye.domain.model.agent.AgentResponseType
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
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
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

    //실제 DB(Room/Firebase)에서 현재 작전 리스트를 1회성으로 안전하게 뽑아오는 코드
    private suspend fun getActiveMissions(): List<Mission> {
        val missionFlowResult = missionUseCases.getMissionList()
            .filterNot { it is MissionResult.Loading || it is MissionResult.NoConstructor } // 로딩 상태는 건너뜀
            .firstOrNull()
        // Success 상태일 때만 실제 작전 리스트(resultData)를 꺼내고, 에러나 빈 값이면 빈 리스트로 방어합니다.
        return if (missionFlowResult is MissionResult.Success) {
            missionFlowResult.resultData
        } else {
            emptyList()
        }
    }

    private fun generateAgentRecommendations() {
        viewModelScope.launch(commonCeh) {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    agentMessage = "요원님의 최근 신체 데이터 및 스캔 결과를 동기화 중입니다"
                )
            }

            val profile = sessionManager.currentUser.value

            if (profile == null) {
                Timber.e("프로필 정보를 찾을 수 없습니다.")
                _uiState.update { it.copy(isLoading = false, error = "프로필을 불러오지 못했습니다.") }
                return@launch
            }

            val loadingJob: Job = launch {
                delay(1500)
                _uiState.update { it.copy(agentMessage = "지난주 작전 수행 이력을 바탕으로 취약점을 분석하고 있습니다") }
                delay(1500)
                _uiState.update { it.copy(agentMessage = "분석 완료. 요원님을 위한 최적의 맞춤형 전술을 수립 중입니다") }
            }

            // 🔥 1. 현재 수행 중인 작전 리스트 확보 (빈 리스트를 실제 DB 조회 코드로 교체해 주십시오!)
            val activeMissions = getActiveMissions()

            // 🔥 2. 하이브리드 AI 추천 엔진 가동
            val pipelineResult = missionUseCases.recommendMission(profile, activeMissions)

            loadingJob.cancel()

            // 🔥 AI의 의도가 RECOMMEND(정기 추천)일 경우에만 마지막 브리핑 시간을 현재로 갱신!
            if (pipelineResult.type == AgentResponseType.RECOMMEND) {
                sessionManager.updateLastBriefingTime(System.currentTimeMillis())
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    responseType = pipelineResult.type,
                    recommendations = pipelineResult.recommendations,
                    agentMessage = pipelineResult.briefingMessage,

                    originalResponseType = pipelineResult.type,
                    originalRecommendations = pipelineResult.recommendations,
                    originalAgentMessage = pipelineResult.briefingMessage,
                    isAdjusted = false
                )
            }
            Timber.d("에이전트 작전 수립 완료: ${pipelineResult.recommendations.size}개, 의도: ${pipelineResult.type}")
        }
    }



    fun acceptMission(mission: Mission) {
        viewModelScope.launch(commonCeh) {
            removeMissionFromList(mission.id)

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
        if (feedback.isBlank()) return

        viewModelScope.launch(commonCeh) {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isAdjustmentMode = false,
                    agentMessage = "요원님의 요청 사항을 반영하여 작전을 재검토 중입니다..."
                )
            }

            val profile = sessionManager.currentUser.value ?: return@launch

            // 🔥 3. 피드백 전송 시에도 현재 수행 중인 작전 리스트 확보
            val activeMissions = getActiveMissions()

            // 🔥 4. activeMissions와 feedback을 순서대로 주입!
            val pipelineResult = missionUseCases.recommendMission(profile, activeMissions, feedback)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    responseType = pipelineResult.type,
                    recommendations = pipelineResult.recommendations,
                    agentMessage = pipelineResult.briefingMessage,
                    isAdjusted = true,
                    adjustmentInput = ""
                )
            }
        }
    }

    fun revertToOriginal() {
        _uiState.update {
            it.copy(
                responseType = it.originalResponseType,
                recommendations = it.originalRecommendations,
                agentMessage = it.originalAgentMessage,
                isAdjusted = false,
                isAdjustmentMode = false,
                adjustmentInput = ""
            )
        }
        showToast("원래의 상태로 복구되었습니다.")
    }

    // AI의 제안(삭제/교체)에 대한 승인/거절 액션 통제
    fun confirmAgentProposal() {
        val currentState = _uiState.value
        when (currentState.responseType) {
            AgentResponseType.CONFIRM_DELETE -> {
                // Todo: 향후 현재 수행중인 미션(Active Missions) 삭제 로직 연결
                showToast("요원님의 요청에 따라 해당 작전을 폐기 처리했습니다.")
                revertToOriginal()
            }
            AgentResponseType.CONFIRM_CHANGE -> {
                // Todo: 향후 작전 교체 로직 연결
                showToast("승인 완료. 새로운 작전으로 교체되었습니다.")
                revertToOriginal()
            }
            else -> {}
        }
    }

    // 🔥 [NEW] UI 작전 2단계에서 누락되었던 취소 버튼 액션 추가
    fun cancelAgentProposal() {
        showToast("명령을 취소하고 기존 상태를 유지합니다.")
        revertToOriginal()
    }

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