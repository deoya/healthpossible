package com.hye.domain.usecase.auth

import com.hye.domain.repository.AuthRepository
import com.hye.domain.result.AuthResult
import jakarta.inject.Inject

class SignUpGuestUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(codename: String): AuthResult<Unit> {
        return authRepository.signUpGuest(codename)
    }
}