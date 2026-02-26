package com.hye.profile.model

import androidx.lifecycle.viewModelScope
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.model.survey.SelectionType
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
    private val profileUseCase: ProfileUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun updateSelectionType(type: SelectionType) {
        Timber.d("온보딩 방식 변경됨: $type")
        _uiState.update { it.copy(selectionType = type) }
    }

    fun toggleSurveyAnswer(questionId: String, option: String, isMultiSelect: Boolean) {
        val currentAnswers = _uiState.value.surveyAnswers.toMutableMap()
        val selectionsForQuestion = currentAnswers[questionId]?.toMutableSet() ?: mutableSetOf()

        if (isMultiSelect) {
            if (selectionsForQuestion.contains(option)) {
                selectionsForQuestion.remove(option)
            } else {
                selectionsForQuestion.add(option)
            }
        } else {
            selectionsForQuestion.clear()
            selectionsForQuestion.add(option)
        }

        currentAnswers[questionId] = selectionsForQuestion
        Timber.d("설문 답변 업데이트 -> 문항: $questionId, 현재 선택됨: $selectionsForQuestion")
        _uiState.update { it.copy(surveyAnswers = currentAnswers) }
    }

    fun saveProfileData(onSuccess: () -> Unit) {
        viewModelScope.launch(commonCeh) {
            Timber.d("프로필 데이터 저장 시작")
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 1. 설문 데이터 추출
            val answers = _uiState.value.surveyAnswers
            val healthGoals = answers["Q1"]?.toList() ?: emptyList()
            val painPoints = answers["Q2"]?.toList() ?: emptyList()

            // 2. Domain Model 생성
            val profile = UserProfile(
                onboardingType = _uiState.value.selectionType,
                healthGoals = healthGoals,
                painPoints = painPoints,
                profileCompletionRate = if (_uiState.value.selectionType == SelectionType.SELF) 10 else 30
            )

            Timber.d("저장할 프로필 데이터: $profile")

            // 3. ROP 스타일 처리, 로그 기록 및 토스트 메시지 출력
            profileUseCase.saveProfileData(profile)
                .onSuccess {
                    Timber.d("✅ 프로필 데이터 저장 성공")
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