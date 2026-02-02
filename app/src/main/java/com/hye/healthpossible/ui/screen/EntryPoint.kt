package com.hye.healthpossible.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hye.healthpossible.R
import com.hye.healthpossible.navigation.bottomBarNavGraphBuilder
import com.hye.healthpossible.ui.component.BottomAppBarItem
import com.hye.healthpossible.ui.component.BottomBar
import com.hye.shared.ui.menu.MenuItem
import com.hye.healthpossible.ui.component.TopBar
import com.hye.shared.ui.button.StyledIconButton
import com.hye.shared.theme.AppTheme
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.util.text

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
                    .padding(bottom = AppTheme.dimens.xxxl)
            ) {
                StyledIconButton(
                    onClick = { showAddMissionMenu = !showAddMissionMenu },
                    modifier = Modifier
                        .background(AppTheme.colors.primary, shape = CircleShape)
                        .padding(AppTheme.dimens.md),
                    icon = {Icon(
                        Icons.Default.AddCircle,
                        contentDescription = null,
                        tint = AppTheme.colors.background,
                        modifier = Modifier.size(AppTheme.dimens.xxl)
                    )}
                )
            }
        }
        // Todo 애니메이션으로 바꾸기
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
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = AppTheme.dimens.dropDownMenuAddMisionPadding)
                    .background(AppTheme.colors.background, RoundedCornerShape(AppTheme.dimens.l))
                    .padding(AppTheme.dimens.md)
            ) {
                MenuItem(R.string.menu_item_add_mission_template.text) {
                    showAddMissionMenu = false
                    navController.navigate(ContentNavRouteDef.MissionRecommendationTab)
                }

                MenuItem(R.string.menu_item_add_mission_self.text) {
                    showAddMissionMenu = false
                    navController.navigate(ContentNavRouteDef.MissionCreationTab)
                }
            }
        }
    }
}