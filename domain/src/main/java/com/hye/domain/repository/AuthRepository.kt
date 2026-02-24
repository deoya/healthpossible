package com.hye.domain.repository

import com.hye.domain.result.AuthResult

interface AuthRepository {
    suspend fun signUpGuest(codename: String): AuthResult<Unit>
    suspend fun checkCodenameDuplication(codename: String): AuthResult<Unit>
    fun isLoggedIn(): Boolean
}