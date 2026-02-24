package com.hye.data.repository.impl

import com.hye.data.datasource.auth.AuthDataSource
import com.hye.data.di.IoDispatcher
import com.hye.domain.repository.AuthRepository
import com.hye.domain.result.AuthResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override suspend fun signUpGuest(codename: String): AuthResult<Unit> {
        return withContext(ioDispatcher) {
            runCatching {
                val uid = authDataSource.signInAnonymously()
                authDataSource.saveUserProfile(uid, codename)
            }.fold(
                onSuccess = { AuthResult.Success(Unit) },
                onFailure = { AuthResult.Failure(it) }
            )
        }
    }

    override suspend fun checkCodenameDuplication(codename: String): AuthResult<Unit> {
        return withContext(ioDispatcher) {
            runCatching {
                val isDuplicated = authDataSource.checkCodenameDuplication(codename)
                if (isDuplicated) {
                    throw Exception("이미 사용 중인 코드네임입니다.")
                }
            }.fold(
                onSuccess = { AuthResult.Success(Unit) },
                onFailure = { AuthResult.Failure(it) }
            )
        }
    }

    override fun isLoggedIn(): Boolean {
        return authDataSource.isLoggedIn()
    }
}