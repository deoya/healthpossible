package com.hye.domain.usecase.mission

import com.hye.domain.common.ExecutionResult
import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.repository.MissionRepository
import com.hye.domain.result.MissionResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateMissionRecordUseCaseTest {
    private val repository: MissionRepository = mockk()

    private lateinit var useCase: UpdateMissionRecordUseCase

    @Before
    fun setUp() {
        useCase = UpdateMissionRecordUseCase(repository)
    }

    @Test
    fun `invoke 호출 시 repository_updateMissionRecord가 호출되어야 한다`() = runTest {
        // Given (준비)
        val dummyRecord = MissionRecord(
            id = "test_id",
            missionId = "mission_1",
            date = "2024-02-05",
            progress = 10,
            isCompleted = false
        )
        val expectedResult = ExecutionResult(true, "성공")

        coEvery { repository.updateMissionRecord(any()) } returns flowOf(MissionResult.Success(expectedResult))

        // When (실행)
        val results = useCase(dummyRecord).toList()

        // Then (검증)
        // 1. 데이터가 정확히 1개 들어왔는지 확인
        assertEquals(1, results.size)

        // 2. 그 데이터가 Success 타입이고, 내용이 맞는지 확인
        val item = results[0]
        assertTrue(item is MissionResult.Success)
        assertEquals(expectedResult, (item as MissionResult.Success).resultData)

        // 3. 레포지토리 함수가 정확한 파라미터로 1번 호출되었는지 확인
        coVerify(exactly = 1) { repository.updateMissionRecord(dummyRecord) }
    }

    @Test
    fun `repository가 에러를 반환하면 useCase도 에러를 반환해야 한다`() = runTest {
        // Given
        val dummyRecord = MissionRecord(id = "error_case", missionId = "m1", date = "2024-02-05")
        val exception = Exception("DB Error")

        // 레포지토리가 Error를 리턴하도록 설정
        coEvery { repository.updateMissionRecord(any()) } returns flowOf(MissionResult.Error(exception))

        // When
        val results = useCase(dummyRecord).toList()

        // Then
        assertEquals(1, results.size)

        val item = results[0]
        assertTrue(item is MissionResult.Error)
        assertEquals(exception, (item as MissionResult.Error).exception)
    }
}