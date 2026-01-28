package com.hye.healthpossible.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, topBarActions: (@Composable () -> Unit)?) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomAppBarItems = remember { BottomAppBarItem.fetchBottomAppBarItems() }

    val titleBarCustom = navBackStackEntry?.topBarAsRouteName ?: TitleBarCustom()

    TopAppBar(
        title = {
            if (titleBarCustom.title != null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    titleBarCustom.title?.invoke()
                }
            } else {
                val tab = bottomAppBarItems.find {
                    currentDestination?.hasRoute(it.destination::class) == true
                }
                val tabName = tab?.tabName?.text ?: ""
                Text(
                    text = tabName,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        navigationIcon = {
            if (currentDestination?.hasRoute(ContentNavRouteDef.MissionCreationTab::class) == true) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            } else if (titleBarCustom.titleIcon != null) {
                Icon(titleBarCustom.titleIcon!!, contentDescription = "Navigation Icon")
            }
        },
        actions = {
            topBarActions?.invoke()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colors.background
        )
    )
}