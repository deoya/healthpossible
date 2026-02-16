package com.hye.healthpossible.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.hye.healthpossible.R
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.ui.icon.AppIcon


data class BottomAppBarItem(
    val tabName: Int,
    val icon: AppIcon,
    val destination: ContentNavRouteDef

) {
    companion object {
        val icons = Icons.Default

        fun fetchBottomAppBarItems() = listOf(

            BottomAppBarItem(
                tabName = R.string.tab_name_community,
                icon = AppIcon.Resource(R.drawable.community_icon),
                destination = ContentNavRouteDef.CommunityTab
            ),
            BottomAppBarItem(
                tabName = R.string.tab_name_mission,
                icon = AppIcon.Resource(R.drawable.mission_icon),
                destination = ContentNavRouteDef.MissionTab
            ),
            BottomAppBarItem(
                tabName = R.string.tab_name_add_mission,
                icon = AppIcon.Vector(icons.Add),
                destination = ContentNavRouteDef.MissionCreationTab
            ),
            BottomAppBarItem(
                tabName = R.string.tab_name_my_page,
                icon = AppIcon.Resource(R.drawable.my_page_icon),
                destination = ContentNavRouteDef.MypageTab
            )
        )
    }
}

data class BottomBarRenderSpec(
    val icon: AppIcon,
    val destination: ContentNavRouteDef
)

fun List<BottomAppBarItem>.resolveMissionFabSpec(
    isMissionScreen: Boolean
): BottomBarRenderSpec {
    return if (isMissionScreen) {
        val createItem = first { it.destination == ContentNavRouteDef.MissionCreationTab }
        BottomBarRenderSpec(
            icon = createItem.icon,
            destination = createItem.destination
        )
    } else {
        val missionItem = first { it.destination == ContentNavRouteDef.MissionTab }
        BottomBarRenderSpec(
            icon = missionItem.icon,
            destination = missionItem.destination
        )
    }
}