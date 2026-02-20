package com.hye.mission.ui.components.form.type.exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hye.domain.model.mission.types.ExerciseRecordMode
import com.hye.features.mission.R
import com.hye.mission.ui.util.UserSwitch
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.button.StyledSwitch
import com.hye.shared.ui.chip.StyledFilterChip
import com.hye.shared.ui.text.LabelMedium
import com.hye.shared.ui.text.LabelSmall
import com.hye.shared.ui.text.TextBody
import com.hye.shared.ui.text.StyledInputField
import com.hye.shared.util.text

@Composable
fun ExerciseSettingForm(
    selectedUnit: ExerciseRecordMode,
    onUnitSelected: (ExerciseRecordMode) -> Unit,
    targetValue: String,
    onTargetValueChange: (String) -> Unit,
    useSupportAgent: Boolean,
    onUseSupportAgentChange: (Boolean) -> Unit,
    selectedExerciseName: String,
    onExerciseTypeClick: () -> Unit
) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxl)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xs)
            ) {
                LabelMedium(R.string.mission_plan_standards_of_implementation.text)
                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)) {
                    ExerciseRecordMode.values().forEach { unit ->
                        StyledFilterChip(
                            selected = unit == selectedUnit,
                            onClick = { onUnitSelected(unit) },
                            label = unit.label,
                            borderSelected = unit == selectedUnit
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)) {
                when (selectedUnit) {
                    ExerciseRecordMode.RUNNING -> InputTargetAount(targetValue, onTargetValueChange)
                    ExerciseRecordMode.SELECTED -> SeleteExciseType(
                        exSelectedName = selectedExerciseName,
                        onSelectClick = onExerciseTypeClick
                    )
                }
            }
            StyledSwitch(
                label = {
                    TextBody(R.string.mission_plan_support_agent.text)
                    LabelSmall(
                        R.string.mission_plan_support_agent_description.text(selectedUnit.agentTask)
                    )
                },
                switchStyle =
                UserSwitch(
                    checked = useSupportAgent,
                    onCheckedChange = onUseSupportAgentChange,
                )
            )
        }
}

@Composable
private fun InputTargetAount(
    targetValue: String,
    onTargetValueChange: (String) -> Unit
) {
    LabelMedium(R.string.mission_plan_goal.text)
    StyledInputField(
            value = targetValue,
            onValueChange = { if (it.all { char -> char.isDigit() }) onTargetValueChange(it) },
            placeholder = R.string.mission_plan_running_goal_placeholder.text,
            keyboardType = KeyboardType.Number
    )
}

@Composable
private fun SeleteExciseType(
    exSelectedName: String,
    onSelectClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        StyledInputField(
                value = exSelectedName,
                onValueChange = {},
                placeholder = R.string.mission_plan_select_excise_type_placeholder.text,
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = AppTheme.colors.textSecondary
                    )
                }
        )
        // 투명한 클릭 영역 오버레이
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(14.dp))
                .clickable(onClick = onSelectClick)
        )
    }
}

@Preview(heightDp = 700, showBackground = true)
@Composable
fun Preview_ExerciseSettingForm() {
        ExerciseSettingForm(
            selectedUnit = ExerciseRecordMode.SELECTED,
            onUnitSelected = {},
            targetValue = "",
            onTargetValueChange = {},
            useSupportAgent = true,
            onUseSupportAgentChange = {},
            selectedExerciseName = "",
            onExerciseTypeClick = {},
        )
}
