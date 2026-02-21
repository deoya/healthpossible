package com.hye.mission.ui.components.form.type

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.hye.features.mission.R
import com.hye.mission.ui.util.SettingRules.MISSION_PLAN_GOAL_SUFFIX
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.text.LabelMedium
import com.hye.shared.ui.text.StyledInputSection
import com.hye.shared.ui.text.TextDescription
import com.hye.shared.ui.text.StyledInputField
import com.hye.shared.util.text

@Composable
fun RoutineSettingForm(
    totalTarget: String,
    onTotalTargetChange: (String) -> Unit,
    stepAmount: String,
    onStepAmountChange: (String) -> Unit,
    unitLabel: String,
    onUnitLabelChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.l)) {
        StyledInputSection(
            label = { LabelMedium(R.string.mission_plan_unit_of_measure.text) },
            text = {StyledInputField(
                value = unitLabel,
                onValueChange = onUnitLabelChange,
                placeholder = R.string.mission_plan_measure_placeholder.text
            )},
        )

        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)) {
                StyledInputSection(
                    label = { LabelMedium(R.string.mission_plan_goal.text) },
                    text = {StyledInputField(
                        value = totalTarget,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() }) onTotalTargetChange(
                                it
                            )
                        },
                        placeholder = R.string.mission_plan_goal_placeholder.text,
                        suffix = unitLabel.take(MISSION_PLAN_GOAL_SUFFIX),
                        keyboardType = KeyboardType.Number
                    )}
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)) {
                StyledInputSection(
                    label = { LabelMedium(R.string.mission_plan_goal_rise.text) },
                    text = {StyledInputField(
                        value = stepAmount ?: 1.toString(),
                        onValueChange = {
                            if (it.all { char -> char.isDigit() }) onStepAmountChange(
                                it
                            )
                        },
                        suffix = unitLabel.take(MISSION_PLAN_GOAL_SUFFIX),
                        keyboardType = KeyboardType.Number
                    )}
                )
            }
        }
        if (stepAmount.isNotEmpty() && unitLabel.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxs)
            ) {
                Icon(Icons.Default.Lightbulb, null, tint = AppTheme.colors.mainColor, modifier = Modifier.size(
                    AppTheme.dimens.md))
                TextDescription(
                    text = R.string.mission_plan_goal_rise_description.text(stepAmount,unitLabel),
                    color = AppTheme.colors.mainColor
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview_DailySettingForm(){
    RoutineSettingForm(
        totalTarget = "",
        onTotalTargetChange = {},
        stepAmount = "",
        onStepAmountChange = {},
        unitLabel = "",
    ) { }
}