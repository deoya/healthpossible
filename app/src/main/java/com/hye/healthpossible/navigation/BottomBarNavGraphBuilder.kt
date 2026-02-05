package com.hye.healthpossible.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hye.mission.ui.screen.ExerciseRecordingScreen
import com.hye.mission.ui.screen.MissionCreationScreen
import com.hye.mission.ui.screen.MissionListScreen
import com.hye.shared.navigation.ContentNavRouteDef

fun NavGraphBuilder.bottomBarNavGraphBuilder(
    navController: NavController,
    setTopBarActions: (@Composable (() -> Unit)?) -> Unit
){
    composable<ContentNavRouteDef.MissionTab> {
        MissionListScreen(
            setTopBarActions = setTopBarActions,
            onNavigateToRecording = { missionId: String ->
                // "이 ID를 가진 ExerciseRecording 화면으로 가라"고 객체를 넘김
                navController.navigate(ContentNavRouteDef.ExerciseRecordingView(missionId))
            }
        )
    }

    composable<ContentNavRouteDef.CommunityTab> {
        //CommunityEntryPoint()
    }

    composable<ContentNavRouteDef.MypageTab> {
        //MypageEntryPoint()
    }

    composable<ContentNavRouteDef.MissionCreationTab> {
        MissionCreationScreen(
            setTopBarActions = setTopBarActions,
        )
    }
    composable<ContentNavRouteDef.MissionRecommendationTab> {
        //MissionRecommendationScreen()
    }

    composable<ContentNavRouteDef.ExerciseRecordingView> { backStackEntry ->
        // 넘어온 데이터(ID) 꺼내기
        val args = backStackEntry.toRoute<ContentNavRouteDef.ExerciseRecordingView>()

        // 실제 화면 연결 (ID 전달)
         ExerciseRecordingScreen(
             missionId = args.missionId,
             onNavigateBack = { navController.popBackStack() }
         )

        // (아직 스크린 파일이 없다면 임시 주석 처리)
    }
}