package com.hye.shared.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme

@Composable
fun StyledButton(
    onClick: () -> Unit,
    shape: Dp = AppTheme.dimens.s,
    containerColor: Color = AppTheme.colors.mainColor,
    contentColor: Color = AppTheme.colors.background,
    elevation: Dp = AppTheme.dimens.zero,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(shape),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(elevation),
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        content()
    }
}

@Composable
fun StyledIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
){
    IconButton(
        onClick = {onClick()},
        modifier = modifier
    ) {
       icon()
    }
}

