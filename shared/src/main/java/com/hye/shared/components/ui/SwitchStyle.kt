package com.hye.shared.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme


data class SwitchStyle(
    val checked : Boolean = false,
    val onCheckedChange: (Boolean) -> Unit,
    val checkedThumbColor: Color,
    val checkedTrackColor: Color,
    val uncheckedThumbColor: Color,
    val uncheckedTrackColor: Color
)

data class SwitchLabelStyle(
    val background: Color? = null,
    val shape: Dp? = null,
    val padding: PaddingValues? = null,
    val verticalAlignment: Alignment.Vertical? = null,
    val horizontalArrangement: Arrangement.Horizontal? = null
)

@Composable
fun StyledSwitch(
    switchLableStyle : SwitchLabelStyle = SwitchLabelStyle(),
    label: @Composable () -> Unit,
    switchStyle: SwitchStyle
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background( switchLableStyle.background ?: AppTheme.colors.backgroundMuted, RoundedCornerShape(switchLableStyle.shape ?: AppTheme.dimens.s))
            .padding(switchLableStyle.padding ?: PaddingValues(horizontal = AppTheme.dimens.md, vertical = AppTheme.dimens.s)),
        verticalAlignment = switchLableStyle.verticalAlignment ?: Alignment.CenterVertically,
        horizontalArrangement = switchLableStyle.horizontalArrangement ?: Arrangement.SpaceBetween
    ) {
        Column {
            label()
        }
        Switch(
            checked = switchStyle.checked,
            onCheckedChange = switchStyle.onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = switchStyle.checkedThumbColor,
                checkedTrackColor = switchStyle.checkedTrackColor,
                uncheckedThumbColor = switchStyle.uncheckedThumbColor,
                uncheckedTrackColor = switchStyle.uncheckedTrackColor

            )
        )
    }
}
