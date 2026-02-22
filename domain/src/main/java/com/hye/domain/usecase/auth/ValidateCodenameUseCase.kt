package com.hye.domain.usecase.auth

import com.hye.domain.validation.AuthValidation.reservedWords
import com.hye.domain.validation.CodeNamePolicy.CODENAME_MAX_LEN
import com.hye.domain.validation.CodeNamePolicy.CODENAME_MIN_LEN
import com.hye.domain.validation.Validator
import javax.inject.Inject

class ValidateCodenameUseCase @Inject constructor() {

    operator fun invoke(codename: String): Result<Unit> {
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
