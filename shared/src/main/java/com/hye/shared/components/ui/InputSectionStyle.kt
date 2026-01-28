package com.hye.shared.components.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme

@Composable
fun StyledInputSection(
    label : @Composable () -> Unit = {},
    style : TextFieldStyle,
    space : Dp = AppTheme.dimens.xxs,
    description : @Composable () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(space)) {
        label()
        StyledTextField(style)
        description()
    }
}