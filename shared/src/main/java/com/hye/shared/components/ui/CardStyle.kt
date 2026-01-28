package com.hye.shared.components.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hye.shared.theme.AppTheme

data class CardStyle(
    val modifier: Modifier? = null,
    val shape: Shape? = null,
    val containerColor: Color? = null,
    val elevation: Dp? = null,
    val border: BorderStroke? = null
)
@Composable
fun StyledCard(
    style: CardStyle? = CardStyle(),
    content: @Composable () -> Unit
) {

    Card(
        modifier = style?.modifier ?: Modifier.fillMaxWidth(),
        shape = style?.shape ?: RoundedCornerShape(AppTheme.dimens.md),
        colors = CardDefaults.cardColors(containerColor = style?.containerColor ?: AppTheme.colors.background),
        elevation = CardDefaults.cardElevation(defaultElevation = style?.elevation ?: AppTheme.dimens.zero),
        border = style?.border ?: BorderStroke(AppTheme.dimens.one, AppTheme.colors.cardBorderColor)
    ) {
        content()
    }
}




