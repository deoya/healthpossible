package com.hye.auth.model

data class AuthUiState(
    val codename: String = "",
    val codenameErrorMessage: String = "",
    val isCodenameValid: Boolean = false,
    val isCheckingCodename: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignUpComplete: Boolean = false
)