package com.hye.domain.repository

import kotlinx.coroutines.flow.Flow

interface MissionPreferencesRepository {
    val weeklyGoalCount: Flow<Int>
    val currentCompletedCount: Flow<Int>
    val isAlwaysOnMissionActive: Flow<Boolean>

    suspend fun updateWeeklyGoalCount(count: Int)
    suspend fun updateCompletedCount(count: Int)
    suspend fun setAlwaysOnMissionActive(isActive: Boolean)
}