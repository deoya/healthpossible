package com.hye.healthpossible.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import com.hye.healthpossible.R
import com.hye.shared.components.ui.LabelSmall
import com.hye.shared.components.ui.MenuLabel
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.util.DateFormatType
import com.hye.shared.util.getCurrentFormattedTime
import com.hye.shared.util.text

data class TitleBarCustom (
    var title: (@Composable () -> Unit)? = null,
    var titleIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    var actions: (@Composable () -> Unit)? = null
)

val NavBackStackEntry.topBarAsRouteName : TitleBarCustom
    get(){
        val dest = this.destination
        val currentDate = getCurrentFormattedTime(DateFormatType.KOREAN_DAY)

        return when {
            dest.hasRoute(ContentNavRouteDef.CommunityTab::class) -> TitleBarCustom()
            dest.hasRoute(ContentNavRouteDef.MissionTab::class) -> TitleBarCustom(title = {
                Column( verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxxs),
                    horizontalAlignment = Alignment.Start
                ){
                    MenuLabel(R.string.top_bar_title_today_mission.text, weight = FontWeight.Normal, size = AppTheme.dimens.xl.toSp)
                    LabelSmall(text=currentDate,fontWeight = FontWeight.Medium)
                }
            }, titleIcon = null, actions = null)

            dest.hasRoute(ContentNavRouteDef.MypageTab::class) -> TitleBarCustom()
            dest.hasRoute(ContentNavRouteDef.MissionCreationTab::class) -> {
                TitleBarCustom(
                    title = {
                        MenuLabel(R.string.tab_name_add_mission.text, size =  TextUnit.Unspecified)
                    },
                    actions = null
                )
            }
            else -> TitleBarCustom()
        }
    }
