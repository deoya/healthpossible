package com.hye.domain.usecase.profile

import com.hye.domain.model.profile.UserProfile
import com.hye.domain.repository.ProfileRepository
import com.hye.domain.result.ProfileResult
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(profile: UserProfile): ProfileResult<Unit> {
        return profileRepository.saveProfileData(profile)
    }
}