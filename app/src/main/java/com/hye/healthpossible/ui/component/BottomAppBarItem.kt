package com.hye.healthpossible.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LaptopMac
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.hye.healthpossible.R
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.util.text

data class BottomAppBarItem(
    val tabName: Int,
    val icon: ImageVector,
    val destination: ContentNavRouteDef

){
    companion object {
        val icons = Icons.Default

        fun fetchBottomAppBarItems() = listOf(

            BottomAppBarItem(
                tabName = R.string.tab_name_community,
                icon = icons.Groups,
                destination = ContentNavRouteDef.CommunityTab
            ),
            BottomAppBarItem(
                tabName = R.string.tab_name_mission,
                icon = icons.LaptopMac,
                destination = ContentNavRouteDef.MissionTab
            ),
            BottomAppBarItem(
                tabName = R.string.tab_name_add_mission,
                icon = icons.LaptopMac,
                destination = ContentNavRouteDef.MissionCreationTab
            ),
            BottomAppBarItem(
                tabName = R.string.tab_name_my_page,
                icon = icons.Person,
                destination = ContentNavRouteDef.MypageTab
            )
        )
    }
}