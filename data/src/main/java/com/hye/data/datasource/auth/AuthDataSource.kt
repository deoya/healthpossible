package com.hye.data.datasource.auth

interface AuthDataSource {
    suspend fun signInAnonymously(): String
    suspend fun saveUserProfile(uid: String, codename: String)
    suspend fun checkCodenameDuplication(codename: String): Boolean
    fun isLoggedIn(): Boolean
}