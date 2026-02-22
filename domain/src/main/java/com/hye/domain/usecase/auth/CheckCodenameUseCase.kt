package com.hye.domain.usecase.auth

import com.hye.domain.repository.AuthRepository
import javax.inject.Inject

class CheckCodenameUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(codename: String): Result<Unit> {
        return authRepository.checkCodenameDuplication(codename)
    }
}