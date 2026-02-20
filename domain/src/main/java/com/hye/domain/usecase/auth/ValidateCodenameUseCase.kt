package com.hye.domain.usecase.auth

import com.hye.domain.validation.AuthValidation.reservedWords
import com.hye.domain.validation.CodeNamePolicy.CODENAME_MAX_LEN
import com.hye.domain.validation.CodeNamePolicy.CODENAME_MIN_LEN
import com.hye.domain.validation.Validator
import javax.inject.Inject

class ValidateCodenameUseCase @Inject constructor() {

    operator fun invoke(codename: String): Result<Unit> {
        // Validator를 사용하여 순차적으로 유효성 검사를 실행합니다.
        // 각 검증 함수는 실패 시 에러 메시지(String)를, 성공 시 null을 반환합니다.
        val errorMessage = Validator.Required(codename)
            ?: Validator.MinLength(codename, CODENAME_MIN_LEN)
            ?: Validator.NotContainSpace(codename)
            ?: Validator.AllowedChar(codename)
            ?: Validator.NotContainReservedWord(codename, reservedWords)

        return if (errorMessage == null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(errorMessage))
        }
    }
}
