package com.hye.mission.ui.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hye.domain.model.mission.MissionCategory
import com.hye.shared.theme.AppTheme

@Composable
fun CategorySelectionTab(
    selectedCategory: MissionCategory?,
    onCategorySelected: (MissionCategory) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MissionCategory.values().forEach { category ->
            val isSelected = selectedCategory == category
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) AppTheme.colors.mainColor else Color.White,
                border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onCategorySelected(category) },
                shadowElevation = if (isSelected) 4.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = category.label,
                        color = if (isSelected) Color.White else AppTheme.colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}