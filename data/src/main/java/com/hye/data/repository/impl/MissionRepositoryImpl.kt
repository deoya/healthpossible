package com.hye.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hye.data.mapper.mapToMission
import com.hye.data.mapper.missionToMap
import com.hye.domain.common.ExecutionResult
import com.hye.domain.model.mission.Mission
import com.hye.domain.repository.MissionRepository
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MissionRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): MissionRepository {

    private val currentUserId = "test_user"

    private fun getMissionCollection() = firestore
        .collection("users")
        .document(currentUserId)
        .collection("missions")

    private fun <T> wrapCRUDOperation(
        operation: suspend () -> T
    ): Flow<MissionResult<T>> = flow {
        emit(MissionResult.Loading)

        val result = operation()

        emit(MissionResult.Success(result))
    }.catch { e ->
        emit(MissionResult.FirebaseError(e))
    }.flowOn(Dispatchers.IO)


    override fun getMissionList(): Flow<MissionResult<List<Mission>>> = flow {
        emit(MissionResult.Loading)

        try {
            val snapshot = getMissionCollection().get().await()

            val missions = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { data ->
                    mapToMission(doc.id, data)
                }
            }

            emit(MissionResult.Success(missions))
        } catch (e: Exception) {
            emit(MissionResult.FirebaseError(e))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMission(id: String): MissionResult<Mission?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMission(id: String): MissionResult<Mission?> {
        TODO("Not yet implemented")
    }

    override fun insertMission(mission: Mission): Flow<MissionResult<ExecutionResult>> = wrapCRUDOperation {
        val missionMap = missionToMap(mission)

        getMissionCollection()
            .document(mission.id)
            .set(missionMap, SetOptions.merge())
            .await()

        ExecutionResult(
            isSuccess = true,
            result = "미션이 성공적으로 등록되었습니다"
        )
    }
    override suspend fun deleteMission(habit: Mission): MissionResult<Unit> {
        TODO("Not yet implemented")
    }
}

