package com.hye.profile.state

import com.hye.domain.model.survey.SelectionType

data class ProfileUiState(
    val selectionType: SelectionType = SelectionType.NONE,
    val surveyAnswers: Map<String, Set<String>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)