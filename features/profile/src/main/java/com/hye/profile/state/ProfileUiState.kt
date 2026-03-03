package com.hye.profile.state

import com.hye.domain.model.profile.ActivityLevel
import com.hye.domain.model.survey.SelectionType
import com.hye.domain.model.survey.SurveyQuestionId

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectionType: SelectionType = SelectionType.SELF,
    val surveyAnswers: Map<SurveyQuestionId, Set<String>> = emptyMap(),

    val activityLevel: ActivityLevel? = null,
    val badHabits: List<String> = emptyList()
)