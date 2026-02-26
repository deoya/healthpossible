package com.hye.domain.model.profile

import com.hye.domain.model.survey.SelectionType

data class UserProfile(
    val onboardingType: SelectionType,
    val healthGoals: List<String>,
    val painPoints: List<String>,
    val profileCompletionRate: Int
)