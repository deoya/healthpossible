package com.hye.shared.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentNavRouteDef : HPNavigation {
    @Serializable
    data object MissionTab : ContentNavRouteDef

    @Serializable
    data object CommunityTab : ContentNavRouteDef

    @Serializable
    data object MypageTab : ContentNavRouteDef

    @Serializable
    data object MissionCreationTab : ContentNavRouteDef

    @Serializable
    data object MissionRecommendationTab: ContentNavRouteDef

    @Serializable
    data class ExerciseRecordingView(val missionId: String) : ContentNavRouteDef

}