package com.hye.healthpossible.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LaptopMac
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.hye.shared.navigation.ContentNavRouteDef

data class BottomAppBarItem(
    val tabName: String = "",
    val icon: ImageVector,
    val destination: ContentNavRouteDef //= ContentNavRouteDef.MissionTab

){
    companion object {
        val icons = Icons.Default
        fun fetchBottomAppBarItems() = listOf(

            BottomAppBarItem(
                tabName = "커뮤니티",
                icon = icons.Groups,
                destination = ContentNavRouteDef.CommunityTab
            ),
            BottomAppBarItem(
                tabName = "미션",
                icon = icons.LaptopMac,
                destination = ContentNavRouteDef.MissionTab
            ),
            BottomAppBarItem(
                tabName = "미션추가",
                icon = icons.LaptopMac,
                destination = ContentNavRouteDef.MissionCreationTab
            ),
            BottomAppBarItem(
                tabName = "마이페이지",
                icon = icons.Person,
                destination = ContentNavRouteDef.MypageTab
            )
        )
    }

}