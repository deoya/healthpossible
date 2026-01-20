package com.hye.mission.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hye.domain.model.mission.ExerciseUnit
import com.hye.mission.ui.util.UserSwitch
import com.hye.shared.components.ui.BodyTextMedium
import com.hye.shared.components.ui.LabelMedium
import com.hye.shared.components.ui.LabelSmall
import com.hye.shared.components.ui.StyledSwitch
import com.hye.shared.components.ui.StyledTextField
import com.hye.shared.components.ui.TextFieldStyle
import com.hye.shared.theme.AppTheme


@Composable
fun ExerciseSettingForm(
    selectedUnit: ExerciseUnit,
    onUnitSelected: (ExerciseUnit) -> Unit,
    targetValue: String,
    onTargetValueChange: (String) -> Unit,
    useTimer: Boolean,
    onUseTimerChange: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.doubleExtraLarge)) {
        // 단위 선택
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            LabelMedium("측정 단위")
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall)) {

                ExerciseUnit.values().forEach { unit ->
                    FilterChip(
                        selected = unit == selectedUnit,
                        onClick = { onUnitSelected(unit) },
                        label = { Text(unit.label, fontWeight = FontWeight.Medium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppTheme.colors.mainColor,
                            selectedLabelColor = AppTheme.colors.selectedLabelColor,
                            containerColor = AppTheme.colors.backgroundMuted,
                            labelColor = AppTheme.colors.textSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = unit == selectedUnit,
                            borderColor = Color.Transparent,
                            selectedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(AppTheme.dimens.mediumSmall)
                    )
                }
            }
        }

        // 목표량 입력
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall)) {
            LabelMedium("목표 설정")
            StyledTextField(TextFieldStyle(
                value = targetValue,
                onValueChange = { if (it.all { char -> char.isDigit() }) onTargetValueChange(it) },
                placeholder = "예: 30",
                //label = selectedUnit.label,
                keyboardType = KeyboardType.Number
            ))
        }

        // 타이머 스위치
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundMuted, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                BodyTextMedium("앱 내 타이머 사용")
                LabelSmall("운동 시간을 앱으로 측정합니다.")
            }
            StyledSwitch(UserSwitch(
                checked = useTimer,
                onCheckedChange = onUseTimerChange,
            ))
        }
    }
}

@Preview
@Composable
fun Preview_ExerciseSettingForm(){
    ExerciseSettingForm(
        selectedUnit = ExerciseUnit.TIME,
        onUnitSelected = {},
        targetValue = "",
        onTargetValueChange = {},
        useTimer = true,
        onUseTimerChange = {}
    )
}