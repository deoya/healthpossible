package com.hye.shared.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.hye.shared.ui.common.IconStyle
import com.hye.shared.ui.common.Orientation
import com.hye.shared.theme.AppTheme

data class MenuStyle (
    val modifier : Modifier? = null,
    val icon : IconStyle? = null,
    val spacedBy : Dp? = null
)

@Composable
fun StyledMenu(
    style: MenuStyle,
    content : @Composable () -> Unit,
    orientation: Orientation = Orientation.Column
){
    val icon: @Composable () -> Unit = {if(style.icon?.image != null)
            Icon(
                imageVector = style.icon.image,
                contentDescription = style.icon.contentDescription,
                tint = style.icon.tint ?: AppTheme.colors.mainColorLight,
                modifier = Modifier.size(style.icon.size?: AppTheme.dimens.xxxl)
        )
    }
    when(orientation){
        Orientation.Column -> {
            Column(
                modifier = style?.modifier ?: Modifier
                    .padding(vertical = AppTheme.dimens.l, horizontal =AppTheme.dimens.xxxxs)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(style.spacedBy ?: AppTheme.dimens.xs)
            ) {
                icon()
                content()
            }
        }
        Orientation.Row -> {
            Row(
                modifier = style?.modifier ?: Modifier
                    .padding(vertical = AppTheme.dimens.l, horizontal = AppTheme.dimens.xxxxs)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(style.spacedBy ?: AppTheme.dimens.xs)
            ) {
                icon()
                content()
            }
        }
    }
    
}