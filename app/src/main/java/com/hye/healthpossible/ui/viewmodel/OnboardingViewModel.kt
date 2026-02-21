package com.hye.healthpossible.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.hye.domain.usecase.auth.CheckCodenameUseCase
import com.hye.domain.usecase.auth.ValidateCodenameUseCase
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val codename: String = "",
    val codenameErrorMessage: String = "",
    val isCodenameValid: Boolean = false,
    val isCheckingCodename: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val validateCodenameUseCase: ValidateCodenameUseCase,
    private val checkCodenameUseCase: CheckCodenameUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    fun updateCodename(codename: String) {
        _uiState.update { it.copy(codename = codename, isCodenameValid = false, codenameErrorMessage = "") }
        if (codename.isBlank()) {
            return
        }
        val localValidationResult = validateCodenameUseCase(codename)
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
                checkCodenameUseCase(codename).getOrThrow()
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
}
