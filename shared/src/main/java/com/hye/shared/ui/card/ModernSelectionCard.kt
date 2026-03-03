package com.hye.shared.ui.card

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.hye.shared.theme.AppTheme

@Composable
fun ModernSelectionCard(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) AppTheme.colors.mainColor else AppTheme.colors.incompleteColor
    val backgroundColor = if (isSelected) AppTheme.colors.mainColorLight else AppTheme.colors.background


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.thumbSize)
            .clickable { onClick() },
        shape = RoundedCornerShape(AppTheme.dimens.md),
        color = backgroundColor,
        border = BorderStroke(AppTheme.dimens.agentBubbleStrokeWidth, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = AppTheme.dimens.l),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) AppTheme.colors.mainColor else AppTheme.colors.textPrimary
            )

            if (isSelected) {
                Icon(Icons.Outlined.CheckCircle, null, tint = AppTheme.colors.mainColor)
            } else {
                Icon(Icons.Outlined.RadioButtonUnchecked, null, tint = AppTheme.colors.mainColorLight)
            }
        }
    }
}

@Composable
fun PageIndicator(totalPages: Int, currentPage: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)) {
        repeat(totalPages) { page ->
            val isSelected = page == currentPage
            val width by animateDpAsState(if (isSelected) AppTheme.dimens.xxl else AppTheme.dimens.xxs, label = "dot")
            val color = if (isSelected) AppTheme.colors.mainColor else AppTheme.colors.mainColorLight

            Box(
                modifier = Modifier
                    .height(AppTheme.dimens.xxs)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
