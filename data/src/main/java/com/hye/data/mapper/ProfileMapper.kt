package com.hye.data.mapper

import com.hye.data.dto.UserProfileDto
import com.hye.domain.model.profile.UserProfile

fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        onboardingType = this.onboardingType,
        healthGoals = this.healthGoals,
        painPoints = this.painPoints,
        profileCompletionRate = this.profileCompletionRate
    )
}

//Todo: User 정보 중 survey를 분리 시켜 저장하면 map으로 update를 하지 않아도 될 것 같음
fun UserProfile.toUpdateMap(): Map<String, Any> {
    return mapOf(
        UserProfileDto.ONBOARDING_TYPE to this.onboardingType.name,
        UserProfileDto.HEALTH_GOALS to this.healthGoals,
        UserProfileDto.PAIN_POINTS to this.painPoints,
        UserProfileDto.PROFILE_COMPLETION_RATE to this.profileCompletionRate
    )
}