package com.hye.data.datasource.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthDataSource {

    private val userCollection = firestore.collection("users")

    override suspend fun signInAnonymously(): String {
        val authResult = auth.signInAnonymously().await()
        return authResult.user?.uid ?: throw Exception("익명 로그인에 실패했습니다.")
    }

    override suspend fun saveUserProfile(uid: String, codename: String) {
        val userData = hashMapOf(
            "codename" to codename,
            "createdAt" to System.currentTimeMillis(),
            "isAnonymous" to true
        )
        userCollection.document(uid).set(userData).await()
    }

    override suspend fun checkCodenameDuplication(codename: String): Boolean {
        val snapshot = userCollection.whereEqualTo("codename", codename).get().await()
        return !snapshot.isEmpty // 비어있지 않다면 중복(true)
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}