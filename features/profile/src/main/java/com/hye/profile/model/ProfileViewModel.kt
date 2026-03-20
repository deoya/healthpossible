package com.hye.profile.model

import androidx.lifecycle.viewModelScope
import com.hye.domain.model.profile.ActivityLevel
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.model.survey.SelectionType
import com.hye.domain.model.survey.SurveyQuestionId
import com.hye.domain.session.SessionManager
import com.hye.domain.usecase.ProfileUseCase
import com.hye.profile.state.ProfileUiState
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun updateSelectionType(type: SelectionType) {
        Timber.d("온보딩 방식 변경됨: $type")
        _uiState.update { it.copy(selectionType = type) }
    }

    fun toggleSurveyAnswer(questionId: SurveyQuestionId, option: String, isMultiSelect: Boolean) {
        _uiState.update { state ->
            val currentAnswers = state.surveyAnswers.toMutableMap()
            val currentSet = currentAnswers[questionId]?.toMutableSet() ?: mutableSetOf()

            if (isMultiSelect) {
                if (currentSet.contains(option)) currentSet.remove(option)
                else currentSet.add(option)
            } else {
                currentSet.clear()
                currentSet.add(option)
            }

            currentAnswers[questionId] = currentSet
            state.copy(surveyAnswers = currentAnswers)
        }
    }

    fun updateActivityLevel(level: ActivityLevel) {
        Timber.d("활동 수준 선택됨: ${level.name}")
        _uiState.update { it.copy(activityLevel = level) }
    }
    fun toggleBadHabit(habit: String) {
        _uiState.update { state ->
            val currentHabits = state.badHabits.toMutableList()
            if (currentHabits.contains(habit)) currentHabits.remove(habit)
            else currentHabits.add(habit)
            state.copy(badHabits = currentHabits)
        }
    }

    fun saveProfileData(onSuccess: () -> Unit) {
        viewModelScope.launch(commonCeh) {
            Timber.d("프로필 데이터 저장 시작")
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 1. 설문 데이터 추출
            val answers = _uiState.value.surveyAnswers
            val healthGoals = answers[SurveyQuestionId.HEALTH_GOAL]?.toList() ?: emptyList()
            val painPoints = answers[SurveyQuestionId.PAIN_POINT]?.toList() ?: emptyList()

            val rawDiseases = answers[SurveyQuestionId.CHRONIC_DISEASE]?.toList() ?: emptyList()
            val chronicDiseases = if (rawDiseases.contains("특별히 없음 (해당 사항 없음)")) emptyList() else rawDiseases

            val currentSession = sessionManager.currentUser.value

            // 2. Domain Model 생성
            val profile = UserProfile(
                uid = currentSession?.uid ?: "",
                codename = currentSession?.codename ?: "",
                onboardingType = _uiState.value.selectionType,
                healthGoals = healthGoals,
                painPoints = painPoints,

                badHabits = _uiState.value.badHabits,
                activityLevel = _uiState.value.activityLevel ?: ActivityLevel.NORMAL,
                chronicDiseases = chronicDiseases,
                profileCompletionRate = if (_uiState.value.selectionType == SelectionType.SELF) 10 else 30
            )

            Timber.d("저장할 프로필 데이터: $profile")

            // 3. ROP 스타일 처리, 로그 기록 및 토스트 메시지 출력
            profileUseCase.saveProfileData(profile)
                .onSuccess {
                    Timber.d("✅ 프로필 데이터 저장 성공")
                    sessionManager.fetchUserProfile()
                    _uiState.update { it.copy(isLoading = false) }
                    showToast("프로필이 성공적으로 저장되었습니다.")

                    onSuccess()
                }
                .onFailure { throwable ->
                    Timber.e(throwable, "❌ 프로필 데이터 저장 실패: ${throwable.message}")
                    _uiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "프로필 저장 중 오류가 발생했습니다.")
                    }
                    showToast(throwable.message ?: "프로필 저장에 실패했습니다. 다시 시도해주세요.")
                }
        }
    }
}