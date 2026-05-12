package com.hye.data.repository.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.hye.domain.repository.MissionPreferencesKeys
import com.hye.domain.repository.MissionPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class MissionPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : MissionPreferencesRepository {

    // 예외 처리 및 데이터 읽기
    private val preferencesFlow: Flow<Preferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    // Flow 형태로 데이터 읽기
    override val weeklyGoalCount: Flow<Int> = preferencesFlow.map { preferences ->
        preferences[MissionPreferencesKeys.WEEKLY_GOAL_COUNT] ?: 0 // 기본값 0
    }

    override val currentCompletedCount: Flow<Int> = preferencesFlow.map { preferences ->
        preferences[MissionPreferencesKeys.CURRENT_COMPLETED_COUNT] ?: 0
    }

    override val isAlwaysOnMissionActive: Flow<Boolean> = preferencesFlow.map { preferences ->
        preferences[MissionPreferencesKeys.IS_ALWAYS_ON_MISSION_ACTIVE] ?: false
    }

    // Suspend 함수로 데이터 쓰기
    override suspend fun updateWeeklyGoalCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[MissionPreferencesKeys.WEEKLY_GOAL_COUNT] = count
        }
    }

    override suspend fun updateCompletedCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[MissionPreferencesKeys.CURRENT_COMPLETED_COUNT] = count
        }
    }

    override suspend fun setAlwaysOnMissionActive(isActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[MissionPreferencesKeys.IS_ALWAYS_ON_MISSION_ACTIVE] = isActive
        }
    }
}