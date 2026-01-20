package com.hye.mission.ui.components.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.hye.shared.components.ui.StyledTextField
import com.hye.shared.components.ui.TextFieldStyle
import com.hye.shared.theme.AppTheme

@Composable
fun InputSection(
    label : @Composable () -> Unit,
    style : TextFieldStyle,
    space: Dp? = null,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space ?: AppTheme.dimens.extraSmall)) {
        label()
        StyledTextField(style)
    }
}