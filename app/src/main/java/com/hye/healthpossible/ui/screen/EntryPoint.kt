package com.hye.healthpossible.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hye.healthpossible.navigation.customNavGraphBuilder
import com.hye.healthpossible.navigation.isFullScreen
import com.hye.healthpossible.navigation.isHideTopBar
import com.hye.healthpossible.ui.component.AddMissionOverlay
import com.hye.healthpossible.ui.component.BottomAppBarItem
import com.hye.healthpossible.ui.component.BottomBar
import com.hye.healthpossible.ui.component.TopBar
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPoint(
    isLoggedIn: Boolean,
    isAgentBadgeVisible: Boolean
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomAppBarItems = remember { BottomAppBarItem.fetchBottomAppBarItems() }

    var topBarActions: (@Composable () -> Unit)? by remember { mutableStateOf(null) }
    var showAddMissionMenu by remember { mutableStateOf(false) }
    var pendingDestination by remember { mutableStateOf<Any?>(null) }

    val startDestination = if (isLoggedIn) ContentNavRouteDef.MissionTab else ContentNavRouteDef.OnboardingTab

    val isFullScreen = currentDestination.isFullScreen()
    val isHideTopBar = currentDestination.isHideTopBar()

    val navigateToPending = {
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

    LaunchedEffect(showAddMissionMenu) {
        if (!showAddMissionMenu && pendingDestination != null) {
            navigateToPending()
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
                        isBadgeVisible = isAgentBadgeVisible,
                        bottomNavItems = bottomAppBarItems,
                        currentDestination = currentDestination,
                        showAddMissionMenu = showAddMissionMenu,
                        onShowAddMissionMenuChange = { showAddMissionMenu = it },
                    )
                }
            },
        ) { paddingValues ->
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

        AddMissionOverlay(
            isVisible = showAddMissionMenu,
            navController = navController,
            onDismiss = {
                showAddMissionMenu = false
                navigateToPending()
            }
        )
    }
}