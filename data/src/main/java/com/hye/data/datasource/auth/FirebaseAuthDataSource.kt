package com.hye.data.datasource.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthDataSource {

    private val userCollection = firestore.collection("users")

    override suspend fun signInAnonymously(): String {
        val authResult = auth.signInAnonymously().await()
        return authResult.user?.uid ?: throw Exception("익명 로그인에 실패했습니다. (UID Null)")
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
        return !snapshot.isEmpty
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}