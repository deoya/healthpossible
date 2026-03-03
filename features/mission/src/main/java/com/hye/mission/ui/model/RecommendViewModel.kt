package com.hye.mission.ui.model

import androidx.lifecycle.viewModelScope
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.result.MissionResult
import com.hye.domain.session.SessionManager
import com.hye.domain.usecase.MissionUseCase
import com.hye.mission.ui.state.RecommendUiState
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 1. 세션 매니저에서 가장 최신의 유저 프로필을 가져옴
            val profile = sessionManager.currentUser.value

            if (profile == null) {
                Timber.e("프로필 정보를 찾을 수 없습니다.")
                _uiState.update { it.copy(isLoading = false, error = "프로필을 불러오지 못했습니다.") }
                return@launch
            }

            // 2. 🔥 온디바이스 AI 추천 엔진 가동! (통증 필터링 -> 습관 매칭 -> 난이도 조절)
            val recommendedMissions = missionUseCases.recommendMission(profile)

            // 3. UI 상태 업데이트
            _uiState.update {
                it.copy(
                    isLoading = false,
                    recommendations = recommendedMissions
                )
            }
            Timber.d("에이전트 미션 추천 완료: ${recommendedMissions.size}개")
        }
    }

    // 🔥 미션 수락 시 로직
    fun acceptMission(mission: Mission) {
        viewModelScope.launch(commonCeh) {
            // 1. 화면의 추천 리스트에서 해당 미션 제거
            removeMissionFromList(mission.id)

            // 🔥 수정됨: 반환 타입인 Flow<MissionResult>에 맞춰 collect 방식으로 변경!
            missionUseCases.insertMission(mission).collect { result ->
                when (result) {
                    is MissionResult.Success -> {
                        showToast("작전이 수락되었습니다. [${mission.title}]")
                        Timber.d("미션 저장 성공: ${mission.title}")
                    }
                    is MissionResult.Error -> {
                        showToast("작전 수락 중 오류가 발생했습니다.")
                        Timber.e(result.exception, "미션 저장 실패: ${result.exception.message}")

                        // Todo : 실패했을 경우 지웠던 미션을 다시 리스트에 복구하는 로직 구현
                    }
                    else -> {}
                }
            }
        }
    }

    // 🔥 미션 보류(거절) 시 로직
    fun rejectMission(mission: Mission) {
        // 단순히 리스트에서 제거하여 다음 미션을 보여줌
        removeMissionFromList(mission.id)
        showToast("해당 작전을 보류합니다.")
    }

    private fun removeMissionFromList(missionId: String) {
        _uiState.update { state ->
            state.copy(
                recommendations = state.recommendations.filterNot { it.id == missionId }
            )
        }
    }
}