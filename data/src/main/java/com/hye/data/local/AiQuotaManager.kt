package com.hye.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiQuotaManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ai_quota_prefs", Context.MODE_PRIVATE)

    // 하루 최대 호출 허용 횟수
    private val DAILY_LIMIT = 3

    // 오늘 날짜인지 확인하고, 아니면 횟수 초기화 후 호출 가능 여부 반환
    fun canCallAi(): Boolean {
        val today = LocalDate.now().toString()
        val savedDate = prefs.getString("LAST_CALL_DATE", "")

        // 날짜가 바뀌었으면 카운트 리셋
        if (today != savedDate) {
            prefs.edit()
                .putString("LAST_CALL_DATE", today)
                .putInt("CALL_COUNT", 0)
                .apply()
        }

        val currentCount = prefs.getInt("CALL_COUNT", 0)
        return currentCount < DAILY_LIMIT
    }

    // AI 호출 성공 시 카운트 1 증가
    fun incrementCallCount() {
        val currentCount = prefs.getInt("CALL_COUNT", 0)
        prefs.edit().putInt("CALL_COUNT", currentCount + 1).apply()
    }
}