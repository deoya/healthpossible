package com.hye.healthpossible.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hye.healthpossible.navigation.bottomBarNavGraphBuilder
import com.hye.healthpossible.ui.component.BottomAppBarItem
import com.hye.healthpossible.ui.component.BottomBar
import com.hye.healthpossible.ui.component.TopBar
import com.hye.shared.theme.AppTheme
import com.hye.shared.navigation.ContentNavRouteDef

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun EntryPoint() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    val bottomAppBarItems = remember { BottomAppBarItem.fetchBottomAppBarItems() }

    var topBarActions: (@Composable () -> Unit)? by remember { mutableStateOf(null) }

    val isMissionTab = currentDestination?.hasRoute(ContentNavRouteDef.MissionTab::class) == true

    val bottomNavitemsToShow = bottomAppBarItems.filter {
        it.destination != if (isMissionTab) ContentNavRouteDef.MissionTab else ContentNavRouteDef.MissionCreationTab
    }
    var showAddMissionMenu by remember { mutableStateOf(false) }

    var pendingDestination by remember { mutableStateOf<Any?>(null) }

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
            containerColor = AppTheme.colors.background,
            topBar = {
                TopBar(navController = navController, topBarActions = topBarActions)
            },
            bottomBar = {
                BottomBar(
                    navController = navController,
                    bottomNavItems = bottomNavitemsToShow,
                    currentDestination = currentDestination,
                    showAddMissionMenu = showAddMissionMenu,
                    onShowAddMissionMenuChange = { showAddMissionMenu = it },
                    onPendingDestinationChange = { pendingDestination = it }
                )
            },
        ) {
            paddingValues ->
            NavHost(
                navController = navController,
                startDestination = ContentNavRouteDef.MissionTab,
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .background(AppTheme.colors.background)
            ) {
                bottomBarNavGraphBuilder(
                    navController = navController,
                    setTopBarActions = { actions ->
                        topBarActions = actions
                    }
                )
            }
        }
        if (isMissionTab) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp)
            ) {
                IconButton(
                    onClick = { showAddMissionMenu = !showAddMissionMenu },
                    modifier = Modifier
                        .background(AppTheme.colors.primary, shape = CircleShape)
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = null,
                        tint = AppTheme.colors.iconColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        // Todo 애니메이션으로 바꾸기
        if (showAddMissionMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
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

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 110.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                MenuItem("템플릿으로 추가") {
                    showAddMissionMenu = false
                    navController.navigate(ContentNavRouteDef.MissionRecommendationTab)
                }

                MenuItem("직접 추가") {
                    showAddMissionMenu = false
                    navController.navigate(ContentNavRouteDef.MissionCreationTab)
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    )
}