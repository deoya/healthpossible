package com.hye.domain.repository

import com.hye.domain.model.profile.UserProfile
import com.hye.domain.result.ProfileResult

interface ProfileRepository {
    suspend fun saveProfileData(profileDto: UserProfile): ProfileResult<Unit>
}