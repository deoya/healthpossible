package com.hye.healthpossible.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.hye.domain.usecase.AuthUseCase
import com.hye.healthpossible.ui.state.OnboardingUiState
import com.hye.healthpossible.ui.state.SelectionType
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


// Todo : 로그, 에러 관리
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    fun updateCodename(codename: String) {
        _uiState.update { it.copy(codename = codename, isCodenameValid = false, codenameErrorMessage = "") }
        if (codename.isBlank()) {
            return
        }
        val localValidationResult = authUseCase.validateCodename(codename)
        if (localValidationResult.isFailure) {
            _uiState.update { it.copy(codenameErrorMessage = localValidationResult.exceptionOrNull()?.message ?: "") }
        }
    }
    fun checkCodenameDuplication() {
        val codename = _uiState.value.codename
        
        if (codename.isBlank() || _uiState.value.codenameErrorMessage.isNotEmpty()) {
            return
        }

        viewModelScope.launch(commonCeh) {
            _uiState.update { it.copy(isCheckingCodename = true) }

            runCatching {
                authUseCase.checkCodename(codename).getOrThrow()
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isCheckingCodename = false,
                        isCodenameValid = true, // 최종 유효성 검사 통과
                        codenameErrorMessage = ""
                    )
                }
            }.onFailure { throwable ->
                 _uiState.update {
                    it.copy(
                        isCheckingCodename = false,
                        isCodenameValid = false,
                        codenameErrorMessage = throwable.message ?: "사용할 수 없는 코드네임입니다."
                    )
                }
            }
        }
    }

    fun signUpGuest() {
        val codename = _uiState.value.codename
        if (!uiState.value.isCodenameValid) return

        viewModelScope.launch(commonCeh) {
            _uiState.update { it.copy(isLoading = true) } // 로딩 시작

            runCatching {
                authUseCase.signUpGuest(codename).getOrThrow()
            }.onSuccess {
                _uiState.update {
                    it.copy(isLoading = false, navigateToNextStep = true) // 성공 시 다음 페이지로
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(isLoading = false, error = throwable.message)
                }
            }
        }
    }
    // 네비게이션 완료 후 상태 초기화 함수
    fun onNavigatedToNextStep() {
        _uiState.update { it.copy(navigateToNextStep = false) }
    }

    fun updateSelectionType(type: SelectionType) {
        _uiState.update { it.copy(selectionType = type) }
    }

}
