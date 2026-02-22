package com.hye.healthpossible.ui.state

data class OnboardingUiState(
    val codename: String = "",
    val codenameErrorMessage: String = "",
    val isCodenameValid: Boolean = false,
    val isCheckingCodename: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigateToNextStep: Boolean = false, // 페이지 이동 트리거

    val selectionType: SelectionType = SelectionType.NONE
)

enum class SelectionType {
    NONE, AI, SELF
}