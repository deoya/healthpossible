package com.hye.healthpossible.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import com.hye.healthpossible.R
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.theme.AppTheme

data class TitleBarCustom (
    var title: (@Composable () -> Unit)? = null,
    var titleIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    var actions: (@Composable () -> Unit)? = null
)

val NavBackStackEntry.topBarAsRouteName : TitleBarCustom
    get(){

        val dest = this.destination

        return when {
            dest.hasRoute(ContentNavRouteDef.CommunityTab::class) -> TitleBarCustom()
            dest.hasRoute(ContentNavRouteDef.MissionTab::class) -> TitleBarCustom(title = { Image(painter = painterResource(id = R.drawable.title), contentDescription = "logo", modifier = Modifier.height(70.dp)) }, titleIcon = null)
            dest.hasRoute(ContentNavRouteDef.MypageTab::class) -> TitleBarCustom()
            dest.hasRoute(ContentNavRouteDef.MissionCreationTab::class) -> {
                TitleBarCustom(title={Text("미션 추가", fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary)},
                    actions = null
                    )
            }
            else -> TitleBarCustom()
        }
    }

@Composable
fun AcionsIconUi(
    modifier: Modifier = Modifier
        .padding(end = 16.dp)
        .size(36.dp)
        .background(Color(0xFFB0BEC5), shape = RoundedCornerShape(8.dp)),
    content: @Composable () -> Unit = {}

){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}