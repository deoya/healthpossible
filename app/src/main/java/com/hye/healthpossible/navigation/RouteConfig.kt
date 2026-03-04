package com.hye.healthpossible.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.hye.shared.navigation.ContentNavRouteDef

private val FullScreenRoutes = listOf(
    ContentNavRouteDef.ExerciseRecordingView::class,
    ContentNavRouteDef.OnboardingTab::class,
)

internal fun NavDestination?.isFullScreen(): Boolean {
    return FullScreenRoutes.any { routeClass ->
        this?.hasRoute(routeClass) == true
    }
}
private val HideTopScreenRoutes = listOf(
    ContentNavRouteDef.MypageTab::class,
    ContentNavRouteDef.MissionRecommendationTab::class,
)


internal fun NavDestination?.isHideTopBar(): Boolean {
    return HideTopScreenRoutes.any { routeClass ->
        this?.hasRoute(routeClass) == true
    }
}