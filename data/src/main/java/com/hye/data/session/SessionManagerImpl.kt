package com.hye.data.session

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hye.data.dto.UserProfileDto
import com.hye.data.mapper.toDomain
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SessionManager {
    // 🔥 앱 전역에서 공유될 유저 프로필 상태 파이프관
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    override val currentUser = _currentUser.asStateFlow()

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
                // 앞서 만든 ProfileMapper를 이용해 DTO -> Domain 모델로 변환
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

    // 부분 업데이트 (설문조사 직후 등 서버 통신 없이 로컬 상태만 갱신할 때 유용)
    override fun updateLocalSession(profile: UserProfile) {
        _currentUser.value = profile
    }

    // 로그아웃, 탈퇴, 초기화 시 세션 날리기
    override fun clearSession() {
        _currentUser.value = null
        Timber.d("UserSessionManager: 세션 초기화됨")
    }
}