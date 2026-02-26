package com.hye.data.datasource.profile

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseProfileDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProfileDataSource {

    override suspend fun saveProfileData(uid: String, data: Map<String, Any>) {
        firestore.collection("users")
            .document(uid)
            .set(data, SetOptions.merge())
            .await()
    }
}