package com.hye.domain.usecase.auth

import javax.inject.Inject

class CheckCodenameUseCase @Inject constructor(
    // TODO: private val authRepository: AuthRepository 주입 필요
) {
    suspend operator fun invoke(codename: String): Result<Unit> {
        // TODO: AuthRepository를 사용하여 서버에 코드네임 중복 검사를 요청하는 로직 구현 필요

        // --- 임시 구현 (컴파일 오류 해결용) ---
        // 테스트를 위해 "admin"이라는 코드네임은 항상 실패하도록 처리합니다.
        if (codename.equals("admin", ignoreCase = true)) {
            return Result.failure(Exception("이미 사용 중인 코드네임입니다."))
        }
        // -------------------------------------

        return Result.success(Unit)
    }
}
