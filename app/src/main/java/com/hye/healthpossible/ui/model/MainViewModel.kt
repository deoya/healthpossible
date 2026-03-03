package com.hye.healthpossible.ui.model

import androidx.lifecycle.viewModelScope
import com.hye.domain.session.SessionManager
import com.hye.domain.usecase.AuthUseCase
import com.hye.healthpossible.ui.state.UiState
import com.hye.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    private val _uiStatus = MutableStateFlow(UiState())
    val uiStatus = _uiStatus.asStateFlow()

    init {
        checkUserAuthStatus()
    }

    private fun checkUserAuthStatus() {
        val isLoggedIn = authUseCase.checkAuthStatus()
        _uiStatus.update { it.copy(isLoggedIn = isLoggedIn) }
        if (isLoggedIn) {
            viewModelScope.launch(commonCeh) {
                sessionManager.fetchUserProfile()
            }
        }

    }

    fun onSplashFinished() {
        _uiStatus.update {
            it.copy(isSplashLoading = false)
        }
    }
}