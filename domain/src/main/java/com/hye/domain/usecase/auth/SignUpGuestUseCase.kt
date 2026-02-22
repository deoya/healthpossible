package com.hye.domain.usecase.auth

import com.hye.domain.repository.AuthRepository
import jakarta.inject.Inject

class SignUpGuestUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(codename: String): Result<Unit> {
        return authRepository.signUpGuest(codename)
    }
}