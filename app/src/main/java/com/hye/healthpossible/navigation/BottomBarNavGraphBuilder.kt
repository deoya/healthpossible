package com.hye.healthpossible.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hye.mission.ui.screen.MissionCreationScreen
import com.hye.mission.ui.screen.MissionListScreen
import com.hye.shared.navigation.ContentNavRouteDef

fun NavGraphBuilder.bottomBarNavGraphBuilder(
    navController: NavController,
    setTopBarActions: (@Composable (() -> Unit)?) -> Unit
){
    composable<ContentNavRouteDef.MissionTab> {
        MissionListScreen(
            setTopBarActions = setTopBarActions
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
            setTopBarActions = setTopBarActions
        )
    }
    composable<ContentNavRouteDef.MissionRecommendationTab> {
        //MissionRecommendationScreen()
    }
}