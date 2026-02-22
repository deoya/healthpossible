package com.hye.healthpossible.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hye.healthpossible.navigation.customNavGraphBuilder
import com.hye.healthpossible.ui.component.AddMissionSpreadMenu
import com.hye.healthpossible.ui.component.BottomAppBarItem
import com.hye.healthpossible.ui.component.BottomBar
import com.hye.healthpossible.ui.component.TopBar
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPoint(
    isLoggedIn: Boolean
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomAppBarItems = remember { BottomAppBarItem.fetchBottomAppBarItems() }

    var topBarActions: (@Composable () -> Unit)? by remember { mutableStateOf(null) }
    var showAddMissionMenu by remember { mutableStateOf(false) }
    var pendingDestination by remember { mutableStateOf<Any?>(null) }

    //Todo : 분리 시킬 것
    val fullScreenRoutes = remember {
        listOf(
            ContentNavRouteDef.ExerciseRecordingView::class,
            ContentNavRouteDef.OnboardingTab::class,
        )
    }

    val startDestination = if(isLoggedIn) ContentNavRouteDef.MissionTab else ContentNavRouteDef.OnboardingTab

    val isFullScreen = fullScreenRoutes.any { routeClass ->
        currentDestination?.hasRoute(routeClass) == true
    }
    val isHideTopBar = currentDestination?.hasRoute<ContentNavRouteDef.MypageTab>() == true
    LaunchedEffect(showAddMissionMenu) {
        if (!showAddMissionMenu && pendingDestination != null) {
            val destination = pendingDestination!!
            pendingDestination = null

            navController.navigate(destination) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = AppTheme.colors.backgroundMuted,
            topBar = {
                if (!isFullScreen && !isHideTopBar) {
                    TopBar(
                        navController = navController,
                        topBarActions = topBarActions,
                        isFullScreen = isFullScreen
                    )
                }
            },
            bottomBar = {
                if (!isFullScreen) {
                    BottomBar(
                        navController = navController,
                        bottomNavItems = bottomAppBarItems,
                        currentDestination = currentDestination,
                        showAddMissionMenu = showAddMissionMenu,
                        onShowAddMissionMenuChange = { showAddMissionMenu = it },
                    )
                }
            },
        ) {
            paddingValues ->



            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxSize()
                    .background(AppTheme.colors.background)
            ) {
                customNavGraphBuilder(
                    navController = navController,
                    setTopBarActions = { actions ->
                        topBarActions = actions
                    }
                )
            }
        }

        if (showAddMissionMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = AppTheme.dimens.bottomBarPadding)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        showAddMissionMenu = false
                        pendingDestination?.let { destination ->
                            navController.navigate(destination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                            pendingDestination = null
                        }
                    }
            )
        }
            val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            val menuBottomPadding = 60.dp + navBarHeight
        AddMissionSpreadMenu(
            isVisible = showAddMissionMenu,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = menuBottomPadding),
            onMenu1Click = {
                showAddMissionMenu = false
                navController.navigate(ContentNavRouteDef.MissionRecommendationTab) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onMenu2Click = {
                showAddMissionMenu = false
                navController.navigate(ContentNavRouteDef.MissionCreationTab) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}