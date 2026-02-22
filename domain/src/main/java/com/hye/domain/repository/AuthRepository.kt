package com.hye.domain.repository

interface AuthRepository {
    suspend fun signUpGuest(codename: String): Result<Unit>
    suspend fun checkCodenameDuplication(codename: String): Result<Unit>
    fun isLoggedIn(): Boolean
}