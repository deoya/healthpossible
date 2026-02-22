package com.hye.healthpossible.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hye.community.screen.CommunityFeedScreen
import com.hye.healthpossible.ui.screen.OnboardingScreen
import com.hye.mission.ui.components.recording.exercise.running.RunningSession
import com.hye.mission.ui.screen.ExerciseRecordingScreen
import com.hye.mission.ui.screen.MissionCreationScreen
import com.hye.mission.ui.screen.MissionListScreen
import com.hye.mission.ui.screen.MissionRecommendationScreen
import com.hye.mypage.screen.MyPageScreen
import com.hye.shared.navigation.ContentNavRouteDef
fun NavGraphBuilder.customNavGraphBuilder(
    navController: NavController,
    setTopBarActions: (@Composable (() -> Unit)?) -> Unit
){

    composable<ContentNavRouteDef.OnboardingTab> {
        OnboardingScreen(
            onOnboardingFinished = {
                navController.navigate(ContentNavRouteDef.MissionTab) {
                    popUpTo(ContentNavRouteDef.OnboardingTab) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }


    composable<ContentNavRouteDef.MissionTab> {
        MissionListScreen(
            setTopBarActions = setTopBarActions,
            onNavigateToRecording = { missionId: String ->
                navController.navigate(ContentNavRouteDef.ExerciseRecordingView(missionId))
            }
        )
    }

    composable<ContentNavRouteDef.CommunityTab> {
        CommunityFeedScreen()
    }

    composable<ContentNavRouteDef.MypageTab> {
        MyPageScreen()
    }

    composable<ContentNavRouteDef.MissionCreationTab> {
        MissionCreationScreen(
            setTopBarActions = setTopBarActions,
        )
    }
    composable<ContentNavRouteDef.MissionRecommendationTab> {
        MissionRecommendationScreen()
    }

    composable<ContentNavRouteDef.ExerciseRecordingView> { backStackEntry ->
        val args = backStackEntry.toRoute<ContentNavRouteDef.ExerciseRecordingView>()

         ExerciseRecordingScreen(
             missionId = args.missionId,
             onNavigateBack = { navController.popBackStack() }
         )

    }
    composable<ContentNavRouteDef.ExerciseRecordingViewToRunning> { backStackEntry ->
        RunningSession()
    }
}