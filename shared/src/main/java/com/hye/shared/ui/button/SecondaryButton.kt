package com.hye.shared.ui.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.hye.shared.theme.AppTheme

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colors.mainColor,
    contentColor: Color = AppTheme.colors.background
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(AppTheme.dimens.s),
        modifier = modifier.height(AppTheme.dimens.backButtonInTopBar),
        elevation = ButtonDefaults.buttonElevation(AppTheme.dimens.xxxxxs)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colors.badgeColor,
    contentColor: Color = AppTheme.colors.badgeContentColor
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(AppTheme.dimens.s),
        modifier = modifier.height(AppTheme.dimens.backButtonInTopBar),
        elevation = ButtonDefaults.buttonElevation(AppTheme.dimens.zero)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}