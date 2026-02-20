package com.hye.mission.ui.components.form.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.hye.domain.model.mission.types.DayOfWeek
import com.hye.features.mission.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.common.selectionBtnColor
import com.hye.shared.ui.common.selectionContentColor
import com.hye.shared.ui.common.selectionFontWeight
import com.hye.shared.ui.text.MenuLabel
import com.hye.shared.ui.text.StyledInputSection
import com.hye.shared.ui.text.TitleMedium
import com.hye.shared.ui.text.StyledInputField
import com.hye.shared.util.text

@Composable
fun CommonInputSection(
    name: String,
    onNameChange: (String) -> Unit,
    selectedDays: Set<DayOfWeek>,
    onDayToggle: (DayOfWeek) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxl)) {

        StyledInputSection(
            label = { TitleMedium(R.string.mission_plan_title.text) },
            text = {
                StyledInputField(
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = R.string.mission_plan_title_placeholder.text
                )
            }
        )


        // 요일 선택
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)) {
            TitleMedium(R.string.mission_plan_select_day.text)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = DayOfWeek.values()
                days.forEach { day ->
                    val isSelected = selectedDays.contains(day)
                    Box(
                        modifier = Modifier
                            .size(AppTheme.dimens.xxxxxxl)
                            .clip(CircleShape)
                            .background(isSelected.selectionBtnColor(deselection = AppTheme.colors.backgroundMuted))
                            .clickable { onDayToggle(day) },
                        contentAlignment = Alignment.Center
                    ) {
                        MenuLabel(
                            text = day.name.first().toString(),
                            color = isSelected.selectionContentColor(),
                            weight = isSelected.selectionFontWeight
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview_CommonInputSection() {
    CommonInputSection(
        name = "",
        onNameChange = {},
        selectedDays = setOf(DayOfWeek.MON, DayOfWeek.WED),
        onDayToggle = {}
    )
}