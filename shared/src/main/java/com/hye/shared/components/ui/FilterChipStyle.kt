package com.hye.shared.components.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme

@Composable
fun StyledFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    selectedContainerColor: Color = AppTheme.colors.mainColor,
    selectedLabelColor: Color = AppTheme.colors.selectedLabelColor,
    containerColor: Color = AppTheme.colors.backgroundMuted,
    labelColor: Color = AppTheme.colors.textSecondary,
    borderEnabled: Boolean = true,
    borderSelected: Boolean,
    borderColor: Color = AppTheme.colors.chipBorderColor,
    borderSelectedColor: Color = AppTheme.colors.chipSelectedColor,
    shape: Dp = AppTheme.dimens.s,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontWeight = FontWeight.Medium) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = selectedContainerColor,
            selectedLabelColor = selectedLabelColor,
            containerColor = containerColor,
            labelColor = labelColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = borderEnabled,
            selected = borderSelected,
            borderColor = borderColor,
            selectedBorderColor = borderSelectedColor
        ),
        shape = RoundedCornerShape(shape)
    )
}