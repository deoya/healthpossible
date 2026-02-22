package com.hye.domain.usecase

import com.hye.domain.usecase.auth.CheckAuthStatusUseCase
import com.hye.domain.usecase.auth.CheckCodenameUseCase
import com.hye.domain.usecase.auth.SignUpGuestUseCase
import com.hye.domain.usecase.auth.ValidateCodenameUseCase
import javax.inject.Inject

data class AuthUseCase @Inject constructor(
    val checkCodename: CheckCodenameUseCase,
    val validateCodename: ValidateCodenameUseCase,
    val signUpGuest: SignUpGuestUseCase,
    val checkAuthStatus: CheckAuthStatusUseCase,
)