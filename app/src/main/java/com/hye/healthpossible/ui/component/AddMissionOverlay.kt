package com.hye.healthpossible.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.theme.AppTheme

@Composable
fun AddMissionOverlay(
    isVisible: Boolean,
    navController: NavHostController,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = AppTheme.dimens.bottomBarPadding)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onDismiss
            )
    )

    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val menuBottomPadding = 60.dp + navBarHeight

    AddMissionSpreadMenu(
        isVisible = isVisible,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = menuBottomPadding),
        onMenu1Click = {
            onDismiss()
            navController.navigate(ContentNavRouteDef.MissionRecommendationTab) {
                launchSingleTop = true
                restoreState = true
            }
        },
        onMenu2Click = {
            onDismiss()
            navController.navigate(ContentNavRouteDef.MissionCreationTab) {
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}