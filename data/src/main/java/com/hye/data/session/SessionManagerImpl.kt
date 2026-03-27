package com.hye.data.session

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hye.data.dto.UserProfileDto
import com.hye.data.mapper.toDomain
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.session.SessionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : SessionManager {

    private val prefs: SharedPreferences = context.getSharedPreferences("healthposable_prefs", Context.MODE_PRIVATE)

    // 유저 프로필 상태 파이프관
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    override val currentUser = _currentUser.asStateFlow()

    private val _lastBriefingTime = MutableStateFlow(prefs.getLong("LAST_BRIEFING_TIME", 0L))
    override val lastBriefingTime = _lastBriefingTime.asStateFlow()

    override suspend fun fetchUserProfile() {
        try {
            val uid = auth.currentUser?.uid
            if (uid == null) {
                Timber.w("UserSessionManager: 로그인된 UID가 없습니다.")
                return
            }

            val snapshot = firestore.collection("users").document(uid).get().await()
            val userProfileDto = snapshot.toObject(UserProfileDto::class.java)

            if (userProfileDto != null) {
                _currentUser.value = userProfileDto.toDomain()
                Timber.d("UserSessionManager: 프로필 로드 성공! 코드네임 -> ${_currentUser.value?.codename}")
            } else {
                Timber.w("UserSessionManager: 유저 문서가 존재하지 않습니다.")
                _currentUser.value = null
            }
        } catch (e: Exception) {
            Timber.e(e, "UserSessionManager: 프로필 로드 실패")
            _currentUser.value = null
        }
    }

    override fun updateLocalSession(profile: UserProfile) {
        _currentUser.value = profile
    }

    override fun clearSession() {
        _currentUser.value = null
        Timber.d("UserSessionManager: 세션 초기화됨")
    }

    // 마지막 브리핑 시간을 로컬에 영구 저장하는 로직
    override suspend fun updateLastBriefingTime(timeMillis: Long) {
        // 1. SharedPreferences 금고에 시간 저장
        prefs.edit().putLong("LAST_BRIEFING_TIME", timeMillis).apply()
        // 2. 앱 내 상태(StateFlow) 즉각 갱신
        _lastBriefingTime.value = timeMillis
        Timber.d("UserSessionManager: 마지막 브리핑 시간 갱신됨 -> $timeMillis")
    }
}