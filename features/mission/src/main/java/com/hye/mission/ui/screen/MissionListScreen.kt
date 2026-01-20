package com.hye.mission.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.hye.domain.result.MissionResult
import com.hye.mission.ui.components.mission.DailyProgressCard
import com.hye.mission.ui.components.mission.UserMissionCard
import com.hye.mission.ui.model.MissionViewModel
import com.hye.shared.components.ui.SectionTitle
import com.hye.shared.mock.todayMissions
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.ScaffoldContentPaddingWithBottomBar


@Composable
fun MissionListScreen(
    viewModel: MissionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiStatus.collectAsStateWithLifecycle()

    // 2. 상태(MissionResult)에 따라 화면 분기
    when (val result = uiState.missionList) {

        // (A) 로딩 중일 때
        is MissionResult.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppTheme.colors.mainColor)
            }
        }

        // (B) 성공했을 때 -> 리스트 표시
        is MissionResult.Success -> {
            val missions = result.resultData // 실제 List<Mission>

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = AppTheme.dimens.ScaffoldContentPaddingWithBottomBar,
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.doubleExtraLarge)
            ) {
                // 1. Daily Progress Card (나중에 실제 데이터 연결 필요)
                item { DailyProgressCard() }

                // 2. Mission List
                item { SectionTitle("수행할 미션 (${missions.size})") }

                if (missions.isEmpty()) {
                    item {
                        Text("등록된 미션이 없습니다. + 버튼을 눌러 추가해보세요!")
                    }
                } else {
                    items(missions) { mission ->
                        UserMissionCard(
                            mission = mission,
                            onClick = { /* 상세 이동 or 완료 처리 */ }
                        )
                    }
                }
            }
        }

        // (C) 에러 발생 시
        is MissionResult.FirebaseError -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("데이터를 불러오지 못했습니다: ${result.exception.message}")
            }
        }

        // (D) 그 외 (초기 상태 등)
        else -> { /* 아무것도 안 함 or 빈 화면 */ }
    }
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentPadding = AppTheme.dimens.ScaffoldContentPaddingWithBottomBar,
//        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.doubleExtraLarge)
//    ) {
//        // 1. Daily Progress Card
//        item { DailyProgressCard() }
//        // 2. Mission List
//        item { SectionTitle("수행할 미션") }
//
//        items(todayMissions) { mission ->
//            UserMissionCard(mission = mission)
//        }
//
//    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun PreviewMissionListScreen() {
//    MaterialTheme {
//        MissionListScreen( {})
//    }
//}
//

