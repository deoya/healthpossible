package com.hye.shared.ui.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hye.shared.theme.AppTheme


@Composable
fun BackButton(
    onBack: () -> Unit,
    color : Color = AppTheme.colors.background,
    applyShadow : Boolean = true,
    modifier: Modifier =  Modifier.padding(top = AppTheme.dimens.backButtonInTopBar, start = AppTheme.dimens.l)
){
    Column(modifier = modifier) {
        Surface(
            color = color,
            shape = CircleShape,
            shadowElevation = if(applyShadow) AppTheme.dimens.xxxxs else 0.dp,
            modifier = Modifier.clickable { onBack() }
        ) {
            Icon(Icons.Default.ArrowBack, null, modifier = Modifier.padding(AppTheme.dimens.xxs))
        }
    }
}