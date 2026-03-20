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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.hye.domain.model.mission.WeeklyMissionState
import com.hye.features.mission.R
import com.hye.mission.ui.components.mission.DailyProgressCard
import com.hye.mission.ui.components.mission.UserMissionCard
import com.hye.mission.ui.model.MissionViewModel
import com.hye.mission.ui.state.MissionState
import com.hye.shared.base.BaseScreenTemplate
import com.hye.shared.mock.MissionMockData
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.ScaffoldContentPaddingWithBottomBar
import com.hye.shared.ui.button.StyledIconButton
import com.hye.shared.ui.icon.CalendarIcon
import com.hye.shared.util.text

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MissionListScreen(
    viewModel: MissionViewModel = hiltViewModel(),
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
    onNavigateBack: () -> Unit = {},
    onNavigateToRecording: (String) -> Unit = {}
) {
    val uiState by viewModel.uiStatus.collectAsStateWithLifecycle()

    BaseScreenTemplate(
        screenName = R.string.mission_list_screen.text,
        viewModel = viewModel,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        setTopBarActions = setTopBarActions,
        onNavigateBack = onNavigateBack,
        topBarActionContent = {
            StyledIconButton(
                onClick = { /* Todo: 이전 날짜 확인 기능 */ },
                modifier = Modifier
                    .padding(end = AppTheme.dimens.xxs)
                    .background(AppTheme.colors.background, CircleShape)
                    .size(AppTheme.dimens.xxxxxxl),
                icon = { CalendarIcon() }
            )
        }
    ) {
        MissionListContent(
            uiState = uiState,
            onMissionClick = viewModel::onStartButtonClicked,
            onNavigateToRecording = onNavigateToRecording
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MissionListContent(
    uiState: MissionState,
    onMissionClick: (WeeklyMissionState) -> Unit,
    onNavigateToRecording: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundMuted)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = AppTheme.dimens.ScaffoldContentPaddingWithBottomBar,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxl)
        ) {
            if (uiState.missions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(AppTheme.dimens.l),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = R.drawable.dont_have_a_mission,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            } else {
                item {
                     DailyProgressCard(
                         totalCount = uiState.totalMissionsCount,
                         completedCount = uiState.completedMissionsCount
                     )
                }

                items(uiState.missions) { item ->
                    UserMissionCard(
                        state = item,
                        onClick = { onMissionClick(item) },
                        onNavigateToRecording = { onNavigateToRecording(item.mission.id) }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MissionListScreen_Preview() {
    val dummyMissions = MissionMockData.getMockMissions()

    val dummyState = MissionState(
        missions = dummyMissions,
        totalMissionsCount = dummyMissions.size,
        completedMissionsCount = dummyMissions.count { it.isDoneToday },
        isLoading = false
    )

    MissionListContent(
        uiState = dummyState,
        onMissionClick = {},
        onNavigateToRecording = {},
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Empty State")
@Composable
fun MissionListScreen_Empty_Preview() {
    MissionListContent(
        uiState = MissionState(missions = emptyList()),
        onMissionClick = {},
        onNavigateToRecording = {},
    )
}