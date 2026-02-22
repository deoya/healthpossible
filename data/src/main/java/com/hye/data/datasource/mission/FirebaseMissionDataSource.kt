package com.hye.data.datasource.mission

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hye.data.model.MissionDto
import com.hye.data.model.MissionRecordDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class FirebaseMissionDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : MissionDataSource {

    private val missionCollection = firestore.collection("missions")
    private val recordCollection = firestore.collection("records")

    override fun getMissions(): Flow<List<MissionDto>> = callbackFlow {
        val listener = missionCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                trySend(snapshot.toObjects(MissionDto::class.java))
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun getMission(missionId: String): MissionDto? {
        return missionCollection.document(missionId).get().await().toObject(MissionDto::class.java)
    }

    override suspend fun addMission(missionDto: MissionDto) {
        val newDocRef = missionCollection.document()
        missionDto.id = newDocRef.id
        newDocRef.set(missionDto).await()
    }

    override suspend fun updateMission(missionDto: MissionDto) {
        missionCollection.document(missionDto.id).set(missionDto, SetOptions.merge()).await()
    }

    override suspend fun deleteMission(missionId: String) {
        missionCollection.document(missionId).delete().await()
    }

    override fun getMissionRecords(date: LocalDate): Flow<List<MissionRecordDto>> = callbackFlow {
        val dateString = date.toString()
        val listener = recordCollection.whereEqualTo("date", dateString)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(MissionRecordDto::class.java))
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun updateMissionRecord(recordDto: MissionRecordDto) {
        recordCollection.document(recordDto.id).set(recordDto, SetOptions.merge()).await()
    }
}
