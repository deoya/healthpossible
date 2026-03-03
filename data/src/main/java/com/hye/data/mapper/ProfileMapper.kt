package com.hye.data.mapper

import com.hye.data.dto.UserProfileDto
import com.hye.domain.model.profile.ActivityLevel
import com.hye.domain.model.profile.UserProfile

fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        uid = this.uid,
        codename = this.codename,
        onboardingType = this.onboardingType,
        healthGoals = this.healthGoals,
        painPoints = this.painPoints,
        profileCompletionRate = this.profileCompletionRate,

        badHabits = this.badHabits ?: emptyList(),
        activityLevel = this.activityLevel?.let {
            runCatching { ActivityLevel.valueOf(it) }.getOrNull()
        }
    )
}

//Todo: User 정보 중 survey를 분리 시켜 저장하면 map으로 update를 하지 않아도 될 것 같음
fun UserProfile.toUpdateMap(): Map<String, Any> {
    val updateMap = mutableMapOf<String, Any>(
        UserProfileDto.ONBOARDING_TYPE to this.onboardingType.name,
        UserProfileDto.HEALTH_GOALS to this.healthGoals,
        UserProfileDto.PAIN_POINTS to this.painPoints,
        UserProfileDto.PROFILE_COMPLETION_RATE to this.profileCompletionRate,

        UserProfileDto.BAD_HABITS to (this.badHabits ?: emptyList())
    )

    // activityLevel이 선택된 경우에만 Map에 추가 (Enum -> String)
    this.activityLevel?.let {
        updateMap[UserProfileDto.ACTIVITY_LEVEL] = it.name
    }

    return updateMap
}