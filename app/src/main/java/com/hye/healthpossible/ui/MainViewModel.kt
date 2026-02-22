package com.hye.healthpossible.ui

import com.hye.domain.usecase.AuthUseCase
import com.hye.healthpossible.ui.state.UiState
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : BaseViewModel() {

    private val _uiStatus = MutableStateFlow(UiState())
    val uiStatus = _uiStatus.asStateFlow()

    init {
        checkUserAuthStatus()
    }

    private fun checkUserAuthStatus() {
        _uiStatus.update {
            it.copy(isLoggedIn = authUseCase.checkAuthStatus())
        }
    }

    fun onSplashFinished() {
        _uiStatus.update {
            it.copy(isSplashLoading = false)
        }
    }
}
