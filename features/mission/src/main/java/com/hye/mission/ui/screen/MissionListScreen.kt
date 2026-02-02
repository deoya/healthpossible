package com.hye.mission.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.features.mission.R
import com.hye.mission.ui.components.mission.DailyProgressCard
import com.hye.mission.ui.components.mission.UserMissionCard
import com.hye.mission.ui.model.MissionViewModel
import com.hye.mission.ui.screen.performing.ExerciseSessionScreen
import com.hye.shared.ui.button.StyledIconButton
import com.hye.shared.ui.text.TitleMedium
import com.hye.shared.ui.icon.CalendarIcon
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.ScaffoldContentPaddingWithBottomBar
import com.hye.shared.util.text


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MissionListScreen(
    viewModel: MissionViewModel = hiltViewModel(),
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
) {
    val uiState by viewModel.uiStatus.collectAsStateWithLifecycle()

    setTopBarActions({
        StyledIconButton(
            onClick = {  }, // Todo: 이전 날짜 확인 가능 기능
            modifier = Modifier
                .padding(end = AppTheme.dimens.xxs)
                .background(AppTheme.colors.background, CircleShape)
                .size(AppTheme.dimens.xxxxxxl),
            icon = { CalendarIcon() })
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundMuted)
    ) {
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppTheme.colors.mainColor)
                }
            }
            uiState.errorMessage != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    //Todo: 에러 대처 코드 추가 예정
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = AppTheme.dimens.ScaffoldContentPaddingWithBottomBar,
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxl)
                ) {
                    item {
                        DailyProgressCard(
                            totalCount = uiState.totalMissionsCount,
                            completedCount = uiState.completedMissionsCount
                        )
                    }

                    item {
                        TitleMedium(R.string.mission_today_missions.text(uiState.missions.size))
                    }

                    if (uiState.missions.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(AppTheme.dimens.l),
                                contentAlignment = Alignment.Center
                            ) {
                                // Todo: 등록된 미션이 없는 경우의 이미지 추가 예정
                            }
                        }
                    } else {
                        items(uiState.missions) { item ->
                            UserMissionCard(
                                missionWrapper = item,
                                onClick = {
                                    viewModel.onStartButtonClicked(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}