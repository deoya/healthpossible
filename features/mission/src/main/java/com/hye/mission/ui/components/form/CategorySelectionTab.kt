package com.hye.mission.ui.components.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.hye.domain.model.mission.types.MissionType
import com.hye.shared.components.ui.common.selectionBorderStroke
import com.hye.shared.components.ui.common.selectionBtnColor
import com.hye.shared.theme.AppTheme

@Composable
fun CategorySelectionTab(
    selectedCategory: MissionType?,
    onCategorySelected: (MissionType) -> Unit,

    itemContent: @Composable (MissionType, Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)
    ) {
        MissionType.values().forEach{ category ->
            val isSelected = selectedCategory == category
            Surface(
                shape = RoundedCornerShape(AppTheme.dimens.md),
                color = isSelected.selectionBtnColor(),
                border = isSelected.selectionBorderStroke(),
                modifier = Modifier
                    .clip(RoundedCornerShape(AppTheme.dimens.md))
                    .clickable { onCategorySelected(category) },
            ) {
                itemContent(category, isSelected)
            }
        }
    }
}

