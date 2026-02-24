package com.hye.auth.model

import androidx.lifecycle.viewModelScope
import com.hye.domain.usecase.AuthUseCase
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun updateCodename(codename: String) {
        _uiState.update { it.copy(codename = codename, isCodenameValid = false, codenameErrorMessage = "") }
        if (codename.isBlank()) return

        authUseCase.validateCodename(codename)
            .onFailure { throwable ->
                val errorMessage = throwable.message ?: ""
                Timber.d("코드네임 로컬 검증 실패: $errorMessage")
                _uiState.update { it.copy(codenameErrorMessage = errorMessage) }
            }
    }

    fun checkCodenameDuplication() {
        val codename = _uiState.value.codename
        if (codename.isBlank() || _uiState.value.codenameErrorMessage.isNotEmpty()) return

        viewModelScope.launch(commonCeh) {
            Timber.d("서버에 코드네임 중복 검사 요청: $codename")
            _uiState.update { it.copy(isCheckingCodename = true) }

            authUseCase.checkCodename(codename)
                .onSuccess {
                    Timber.d("✅ 코드네임 사용 가능: $codename")
                    _uiState.update {
                        it.copy(isCheckingCodename = false, isCodenameValid = true, codenameErrorMessage = "")
                    }
                }
                .onFailure { throwable ->
                    Timber.e(throwable, "❌ 코드네임 중복 검사 실패: ${throwable.message}")
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

    fun signUpGuest(onSuccess: () -> Unit) {
        val codename = _uiState.value.codename
        if (!_uiState.value.isCodenameValid) return

        viewModelScope.launch(commonCeh) {
            Timber.d("익명 회원가입 시작 - 코드네임: $codename")
            _uiState.update { it.copy(isLoading = true) }

            authUseCase.signUpGuest(codename)
                .onSuccess {
                    Timber.d("✅ 회원가입 성공")
                    _uiState.update { it.copy(isLoading = false, isSignUpComplete = true) }

                    showToast("환영합니다! 성공적으로 가입되었습니다.")
                    onSuccess()
                }
                .onFailure { throwable ->
                    Timber.e(throwable, "❌ 회원가입 실패: ${throwable.message}")
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }

                    showToast(throwable.message ?: "회원가입에 실패했습니다. 다시 시도해주세요.")
                }
        }
    }
}