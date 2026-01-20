package com.hye.shared.components.ui

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class SwitchStyle(
    val checked : Boolean = false,
    val onCheckedChange: (Boolean) -> Unit,
    val checkedThumbColor: Color,
    val checkedTrackColor: Color,
    val uncheckedThumbColor: Color,
    val uncheckedTrackColor: Color
)

@Composable
fun StyledSwitch(style: SwitchStyle) = Switch(
    checked = style.checked,
    onCheckedChange = style.onCheckedChange,
    colors = SwitchDefaults.colors(
        checkedThumbColor = style.checkedThumbColor,
        checkedTrackColor = style.checkedTrackColor,
        uncheckedThumbColor = style.uncheckedThumbColor,
        uncheckedTrackColor = style.uncheckedTrackColor

    )
)