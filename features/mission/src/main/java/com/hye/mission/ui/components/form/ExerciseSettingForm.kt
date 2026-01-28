package com.hye.mission.ui.components.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.hye.domain.model.mission.types.ExerciseUnit
import com.hye.features.mission.R
import com.hye.mission.ui.util.UserSwitch
import com.hye.mission.ui.util.agentTask
import com.hye.shared.components.ui.TextBody
import com.hye.shared.components.ui.LabelMedium
import com.hye.shared.components.ui.LabelSmall
import com.hye.shared.components.ui.StyledFilterChip
import com.hye.shared.components.ui.StyledSwitch
import com.hye.shared.components.ui.StyledTextField
import com.hye.shared.components.ui.TextFieldStyle
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.text


@Composable
fun ExerciseSettingForm(
    selectedUnit: ExerciseUnit,
    onUnitSelected: (ExerciseUnit) -> Unit,
    targetValue: String,
    onTargetValueChange: (String) -> Unit,
    useSupportAgent: Boolean,
    onUseSupportAgentChange: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxl)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xs)) {
            LabelMedium(R.string.mission_plan_unit_of_measure.text)
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)) {
                ExerciseUnit.values().forEach { unit ->
                    StyledFilterChip(
                        selected = unit == selectedUnit,
                        onClick = { onUnitSelected(unit) },
                        label = unit.label,
                        borderSelected = unit == selectedUnit
                    )
                }
            }
        }

        // 목표량 입력
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)) {
            LabelMedium(R.string.mission_plan_goal.text)
            StyledTextField(TextFieldStyle(
                value = targetValue,
                onValueChange = { if (it.all { char -> char.isDigit() }) onTargetValueChange(it) },
                placeholder = R.string.mission_plan_goal_placeholder.text,
                keyboardType = KeyboardType.Number
            ))
        }

        StyledSwitch(
            label = {
                TextBody(R.string.mission_plan_support_agent.text)
                LabelSmall(
                    R.string.mission_plan_support_agent_description.text(selectedUnit.agentTask.text)
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

@Preview
@Composable
fun Preview_ExerciseSettingForm(){
    ExerciseSettingForm(
        selectedUnit = ExerciseUnit.TIME,
        onUnitSelected = {},
        targetValue = "",
        onTargetValueChange = {},
        useSupportAgent = true,
        onUseSupportAgentChange = {}
    )
}