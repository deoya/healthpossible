package com.hye.mission.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hye.domain.model.mission.DayOfWeek
import com.hye.shared.components.ui.TextFieldStyle
import com.hye.shared.components.ui.TitleMedium
import com.hye.shared.theme.AppTheme

@Composable
fun CommonInputSection(
    name: String,
    onNameChange: (String) -> Unit,
    selectedDays: Set<DayOfWeek>,
    onDayToggle: (DayOfWeek) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.doubleExtraLarge)) {
        InputSection(
            label = {TitleMedium("미션 이름")},
            TextFieldStyle(
            value = name,
            onValueChange = onNameChange,
            placeholder = "예: 아침 조깅, 물 마시기"
        ))


        // 요일 선택
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.mediumSmall)) {
            Text("반복 요일", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = DayOfWeek.values()
                days.forEach { day ->
                    val isSelected = selectedDays.contains(day)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) AppTheme.colors.mainColor else AppTheme.colors.backgroundMuted)
                            .clickable { onDayToggle(day) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.name.first().toString(),
                            color = if (isSelected) AppTheme.colors.background else AppTheme.colors.textSecondary,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview_CommonInputSection(){
    CommonInputSection(
        name = "미션 이름",
        onNameChange = {},
        selectedDays = setOf(DayOfWeek.MON, DayOfWeek.WED),
        onDayToggle = {}
    )
}