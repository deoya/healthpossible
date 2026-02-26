package com.hye.data.datasource.profile


interface ProfileDataSource {
    suspend fun saveProfileData(uid: String, data: Map<String, Any>)
}