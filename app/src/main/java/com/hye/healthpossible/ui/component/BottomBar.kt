package com.hye.healthpossible.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.text

@Composable
fun BottomBar(
    navController: NavController,
    bottomNavItems: List<BottomAppBarItem>,
    currentDestination: NavDestination?,
    showAddMissionMenu: Boolean,
    onShowAddMissionMenuChange: (Boolean) -> Unit,
    onPendingDestinationChange: (Any?) -> Unit
) {
    NavigationBar(
        containerColor = AppTheme.colors.background,
        tonalElevation = AppTheme.dimens.xxs
    ) {
        bottomNavItems.forEach { item ->
            val isSelected =
                currentDestination?.hasRoute(item.destination::class) == true
            val isAddMissionTab =
                item.destination == ContentNavRouteDef.MissionCreationTab
            
            NavigationBarItem(
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppTheme.colors.primary,
                    selectedTextColor = AppTheme.colors.primary,
                    indicatorColor = AppTheme.colors.primaryLight,
                    unselectedIconColor = AppTheme.colors.inactive,
                    unselectedTextColor = AppTheme.colors.inactive
                ),
                label = { Text(item.tabName.text) },
                icon = { Icon(item.icon, contentDescription = null) },
                onClick = {
                    if (showAddMissionMenu) {
                        onPendingDestinationChange(item.destination)
                        onShowAddMissionMenuChange(false)
                        return@NavigationBarItem
                    }

                    if (isAddMissionTab) {
                        onShowAddMissionMenuChange(true)
                    } else {
                        navController.navigate(item.destination) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}