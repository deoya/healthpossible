package com.hye.data.repository.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class MissionPreferencesRepositoryImplTest {

    // 1. 임시 폴더 생성 (테스트 종료 시 자동 삭제됨)
    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: MissionPreferencesRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @Before
    fun setup() {
        // 2. 가짜(테스트용) DataStore 파일 생성
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(tmpFolder.root, "test_mission_prefs.preferences_pb") }
        )

        // 3. 테스트할 Repository에 가짜 금고 주입
        repository = MissionPreferencesRepositoryImpl(dataStore)
    }

    @Test
    fun `주간 목표 횟수를 업데이트하면 Flow에서 새 값이 방출되어야 한다`() = runTest {
        // Given (상황 부여): 목표 횟수를 5회로 설정하라는 명령 하달
        val targetGoal = 5

        // When (행동): Repository를 통해 금고에 값 저장
        repository.updateWeeklyGoalCount(targetGoal)

        // Then (검증): 금고에서 Flow를 읽어왔을 때 5가 나와야 작전 성공
        val currentGoal = repository.weeklyGoalCount.first()
        assertEquals(targetGoal, currentGoal)
    }

    @Test
    fun `상시 미션 활성화 여부를 토글하면 Flow에 즉각 반영되어야 한다`() = runTest {
        // Given
        val isAlwaysOn = true

        // When
        repository.setAlwaysOnMissionActive(isAlwaysOn)

        // Then
        val result = repository.isAlwaysOnMissionActive.first()
        assertEquals(true, result)
    }
}