package com.hye.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.hye.data.datasource.profile.ProfileDataSource
import com.hye.data.di.IoDispatcher
import com.hye.data.mapper.toUpdateMap
import com.hye.domain.model.profile.UserProfile
import com.hye.domain.repository.ProfileRepository
import com.hye.domain.result.ProfileResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val auth: FirebaseAuth,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ProfileRepository {

    override suspend fun saveProfileData(profile: UserProfile): ProfileResult<Unit> {
        return withContext(ioDispatcher) {
            runCatching {
                val uid = auth.currentUser?.uid ?: throw Exception("사용자 정보가 없습니다.")

                val updateData = profile.toUpdateMap()

                profileDataSource.saveProfileData(uid, updateData)

            }.fold(
                onSuccess = { ProfileResult.Success(Unit) },
                onFailure = { ProfileResult.Failure(it) }
            )
        }
    }
}