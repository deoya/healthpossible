package com.hye.domain.usecase

import com.hye.domain.usecase.profile.SaveProfileUseCase
import javax.inject.Inject

data class ProfileUseCase @Inject constructor(
    val saveProfileData: SaveProfileUseCase,
)