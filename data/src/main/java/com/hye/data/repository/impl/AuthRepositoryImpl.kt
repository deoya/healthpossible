package com.hye.data.repository.impl

import com.hye.data.datasource.auth.AuthDataSource
import com.hye.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override suspend fun signUpGuest(codename: String): Result<Unit> {
        return runCatching {
            val uid = authDataSource.signInAnonymously()
            authDataSource.saveUserProfile(uid, codename)
        }
    }

    override suspend fun checkCodenameDuplication(codename: String): Result<Unit> {
        return runCatching {
            val isDuplicated = authDataSource.checkCodenameDuplication(codename)
            if (isDuplicated) {
                throw Exception("이미 사용 중인 코드네임입니다.")
            }
        }
    }

    override fun isLoggedIn(): Boolean {
        return authDataSource.isLoggedIn()
    }
}