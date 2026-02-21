package com.hye.data.repository.impl

import com.hye.data.datasource.MissionDataSource
import com.hye.data.di.IoDispatcher
import com.hye.data.mapper.toDomain
import com.hye.data.mapper.toDto
import com.hye.data.mapper.toRecordDomain
import com.hye.data.mapper.toRecordDto
import com.hye.domain.common.ExecutionResult
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.model.mission.types.Mission
import com.hye.domain.repository.MissionRepository
import com.hye.domain.result.MissionResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject



class MissionRepositoryImpl @Inject constructor(
    private val missionDataSource: MissionDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MissionRepository {

    private fun <T> resultFlow(block: suspend () -> T): Flow<MissionResult<T>> = flow {
        emit(MissionResult.Loading)

        runCatching {
            block()
        }.onSuccess { result ->
            emit(MissionResult.Success(result))
        }.onFailure { e ->
            Timber.e(e, "Repository operation failed: ${e.message}")
            emit(MissionResult.Error(e as Exception))
        }
    }.flowOn(ioDispatcher)

    override fun getMissionList(): Flow<MissionResult<List<Mission>>> {
        return missionDataSource.getMissions()
            .map { dtoList ->
                val domainList = dtoList.map { it.toDomain() }
                MissionResult.Success(domainList) as MissionResult<List<Mission>>
            }
            .catch { e ->
                Timber.e(e, "getMissionList failed")
                emit(MissionResult.Error(e as Exception))
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun getMission(id: String): MissionResult<Mission?> {
        return withContext(ioDispatcher) {
            runCatching {
                val dto = missionDataSource.getMission(id)
                dto?.toDomain()
            }.fold(
                onSuccess = { MissionResult.Success(it) },
                onFailure = {
                    Timber.e(it, "getMission failed")
                    MissionResult.Error(it as Exception)
                }
            )
        }
    }

    override fun insertMission(mission: Mission): Flow<MissionResult<ExecutionResult>> = resultFlow {
        missionDataSource.addMission(mission.toDto())
        ExecutionResult(true, "미션이 성공적으로 등록되었습니다")
    }

    override fun updateMission(mission: Mission): Flow<MissionResult<ExecutionResult>> = resultFlow {
        missionDataSource.updateMission(mission.toDto())
        ExecutionResult(true, "미션이 성공적으로 수정되었습니다")
    }

    override fun deleteMission(missionId: String): Flow<MissionResult<ExecutionResult>> = resultFlow {
        missionDataSource.deleteMission(missionId)
        ExecutionResult(true, "미션이 삭제되었습니다")
    }

    override fun getMissionRecords(date: LocalDate): Flow<MissionResult<List<MissionRecord>>> {
        return missionDataSource.getMissionRecords(date)
            .map { dtoList ->
                val domainList = dtoList.map { it.toRecordDomain() }
                MissionResult.Success(domainList) as MissionResult<List<MissionRecord>>
            }
            .catch { e ->
                Timber.e(e, "getMissionRecords failed")
                emit(MissionResult.Error(e as Exception))
            }
            .flowOn(ioDispatcher)
    }

    override fun updateMissionRecord(record: MissionRecord): Flow<MissionResult<ExecutionResult>> = resultFlow {
        missionDataSource.updateMissionRecord(record.toRecordDto())
        ExecutionResult(true, "기록이 업데이트되었습니다")
    }
}
