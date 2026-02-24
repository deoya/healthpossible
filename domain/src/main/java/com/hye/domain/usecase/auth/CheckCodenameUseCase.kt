package com.hye.domain.usecase.auth

import com.hye.domain.repository.AuthRepository
import com.hye.domain.result.AuthResult
import javax.inject.Inject

class CheckCodenameUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(codename: String): AuthResult<Unit> {
        return authRepository.checkCodenameDuplication(codename)
    }
}