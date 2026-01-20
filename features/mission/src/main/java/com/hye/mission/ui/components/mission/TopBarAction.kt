package com.hye.mission.ui.components.mission

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight


@Composable
fun TopBarAction (
    onClick: () -> Unit,
    enabled : Boolean,
    label : String,
    color : Color,
){
    TextButton(
        onClick = onClick,
        enabled = enabled
    ) {
        Text(
            label,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}