package com.hye.shared.components.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

data class ButtonStyle (
    val onClick : () -> Unit,
    val shape : Shape,
    val containerColor: Color,
    val contentColor: Color,
    val elevation : Dp,
    val contentPadding : PaddingValues,
    val modifier : Modifier,
)

@Composable
fun StyledButton(
    style : ButtonStyle,
    content : @Composable () -> Unit
){

    Button(
        onClick = style.onClick,
        shape = style.shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = style.containerColor,
            contentColor = style.contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(style.elevation),
        contentPadding = style.contentPadding,
        modifier = style.modifier
    ) {
        content()
    }

}

