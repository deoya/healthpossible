package com.hye.domain.session

import com.hye.domain.model.profile.UserProfile
import kotlinx.coroutines.flow.StateFlow

interface SessionManager {
    val currentUser: StateFlow<UserProfile?>
    suspend fun fetchUserProfile()
    fun updateLocalSession(profile: UserProfile)
    fun clearSession()
}