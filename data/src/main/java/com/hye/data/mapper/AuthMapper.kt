package com.hye.data.mapper

import com.hye.data.dto.UserProfileDto

fun String.toInitialProfileMap(): Map<String, Any> {
    return mapOf(
        UserProfileDto.CODENAME to this,
        UserProfileDto.CREATED_AT to System.currentTimeMillis(),
        UserProfileDto.IS_ANONYMOUS to true,
        UserProfileDto.PROFILE_COMPLETION_RATE to 10
    )
}