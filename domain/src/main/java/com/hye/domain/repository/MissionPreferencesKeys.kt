package com.hye.domain.repository

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object MissionPreferencesKeys {
    val WEEKLY_GOAL_COUNT = intPreferencesKey("weekly_goal_count")
    val CURRENT_COMPLETED_COUNT = intPreferencesKey("current_completed_count")
    val IS_ALWAYS_ON_MISSION_ACTIVE = booleanPreferencesKey("is_always_on_mission_active")
}