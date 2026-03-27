package com.hye.domain.session

import com.hye.domain.model.profile.UserProfile
import kotlinx.coroutines.flow.StateFlow

interface SessionManager {
    val currentUser: StateFlow<UserProfile?>
    suspend fun fetchUserProfile()
    fun updateLocalSession(profile: UserProfile)
    fun clearSession()

    // 1. 마지막 브리핑 시간을 관찰할 수 있는 파이프라인
    val lastBriefingTime: StateFlow<Long>

    // 2. 브리핑을 완료했을 때 시간을 로컬 DB에 갱신하는 함수
    suspend fun updateLastBriefingTime(timeMillis: Long)

    // 3. 브리핑이 필요한지(7일이 지났는지) 계산하는 편의 함수
    fun isWeeklyBriefingNeeded(lastTime: Long): Boolean {
        if (lastTime == 0L) return true
        val sevenDaysInMillis = 7L * 24 * 60 * 60 * 1000 // 7일을 밀리초로 변환
        return (System.currentTimeMillis() - lastTime) >= sevenDaysInMillis
    }
}