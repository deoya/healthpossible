package com.hye.healthpossible.ui.state

data class UiState (
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val isSplashLoading: Boolean = true,

    val isLoggedIn : Boolean = false
)
