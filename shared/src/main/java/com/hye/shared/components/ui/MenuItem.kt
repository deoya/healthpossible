package com.hye.shared.components.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp

@Composable
fun MenuItem(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = AppTheme.dimens.md.toSp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.dimens.s)
    )
}
