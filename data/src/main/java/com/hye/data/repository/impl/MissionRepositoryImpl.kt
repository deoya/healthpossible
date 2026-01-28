package com.hye.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hye.data.mapper.mapToMission
import com.hye.data.mapper.missionToMap
import com.hye.domain.common.ExecutionResult
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.repository.MissionRepository
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    // [추가] 수행 기록 컬렉션 (users/{uid}/records)
    private fun getRecordCollection() = firestore
        .collection("users")
        .document(currentUserId)
        .collection("records")

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

    override fun getMissionRecords(date: String): Flow<MissionResult<List<MissionRecord>>> =
        callbackFlow {
            trySend(MissionResult.Loading)

            val listener = getRecordCollection()
                .whereEqualTo("date", date) // 해당 날짜의 기록만 필터링
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        trySend(MissionResult.FirebaseError(e))
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val records = snapshot.documents.map { doc ->
                            MissionRecord(
                                id = doc.id,
                                missionId = doc.getString("missionId") ?: "",
                                date = doc.getString("date") ?: "",
                                progress = doc.getLong("progress")?.toInt() ?: 0,
                                isCompleted = doc.getBoolean("isCompleted") ?: false,
                                completedAt = doc.getString("completedAt")
                            )
                        }
                        trySend(MissionResult.Success(records))
                    }
                }

            awaitClose { listener.remove() }
        }
    // [구현] 기록 업데이트 (없으면 생성, 있으면 갱신)
    override fun updateMissionRecord(record: MissionRecord): Flow<MissionResult<ExecutionResult>> = flow {
        emit(MissionResult.Loading)
        try {
            // Firestore에 저장 (문서 ID를 지정하여 덮어쓰기)
            getRecordCollection()
                .document(record.id)
                .set(record, SetOptions.merge()) // data class 그대로 저장 가능 (Firestore KTX 지원 시)
                .await()

            emit(MissionResult.Success(ExecutionResult(true, "기록 업데이트 성공")))
        } catch (e: Exception) {
            emit(MissionResult.FirebaseError(e))
        }
    }
}

