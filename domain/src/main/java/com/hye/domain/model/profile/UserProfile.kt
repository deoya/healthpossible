package com.hye.domain.model.profile

import com.hye.domain.model.survey.SelectionType

data class UserProfile(
    val uid: String,
    val codename: String,
    val onboardingType: SelectionType,
    val healthGoals: List<String>,
    val painPoints: List<String>,
    val profileCompletionRate: Int,
    val badHabits: List<String>? = emptyList(), // 없을 수도 있으므로 nullable 또는 빈 리스트
    val activityLevel: ActivityLevel? = null,
    val chronicDiseases: List<String>? = emptyList()
)

enum class ActivityLevel(val label: String) {
    BEGINNER("기초 가동 단계(매우 적음)"),
    NORMAL("일반 작전 수행 가능(보통)"),
    EXPERT("고도화된 작전 수행 가능(활동적임)")
}