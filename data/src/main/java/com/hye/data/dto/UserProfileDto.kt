package com.hye.data.dto

import com.hye.domain.model.survey.SelectionType

data class UserProfileDto(
    val uid: String = "",
    val codename: String = "",
    val onboardingType: SelectionType = SelectionType.SELF,
    val healthGoals: List<String> = emptyList(),
    val painPoints: List<String> = emptyList(),
    val activityLevel: String? = null,
    val preferredTime: String? = null,
    val badHabits: List<String>? = null,
    val sleepPattern: String? = null,
    val profileCompletionRate: Int = 10,
    val createdAt: Long = System.currentTimeMillis(),
    val isAnonymous: Boolean = true
) {
    companion object Fields {
        const val UID = "uid"
        const val CODENAME = "codename"
        const val ONBOARDING_TYPE = "onboardingType"
        const val HEALTH_GOALS = "healthGoals"
        const val PAIN_POINTS = "painPoints"
        const val ACTIVITY_LEVEL = "activityLevel"
        const val PREFERRED_TIME = "preferredTime"
        const val BAD_HABITS = "badHabits"
        const val SLEEP_PATTERN = "sleepPattern"
        const val PROFILE_COMPLETION_RATE = "profileCompletionRate"
        const val CREATED_AT = "createdAt"
        const val IS_ANONYMOUS = "isAnonymous"
    }
}