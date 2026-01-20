package com.hye.mission.ui.components.form

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
import com.hye.shared.components.ui.BodyTextSmall
import com.hye.shared.components.ui.LabelMedium
import com.hye.shared.components.ui.TextFieldStyle
import com.hye.shared.theme.AppTheme


@Composable
fun DailySettingForm(
    totalTarget: String,
    onTotalTargetChange: (String) -> Unit,
    stepAmount: String,
    onStepAmountChange: (String) -> Unit,
    unitLabel: String,
    onUnitLabelChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraLarge)) {
        InputSection(
            label = { LabelMedium("단위 이름") },
            style = TextFieldStyle(
                value = unitLabel,
                onValueChange = onUnitLabelChange,
                placeholder = "예: ml, 잔, 페이지"
            ),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.mediumSmall)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall)) {
                InputSection(
                    label = { LabelMedium("하루 총 목표") },
                    style = TextFieldStyle(
                        value = totalTarget,
                        onValueChange = { if (it.all { char -> char.isDigit() }) onTotalTargetChange(it) },
                        placeholder = "2000",
                        suffix = unitLabel.take(3),
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall)) {
                InputSection(
                    label = { LabelMedium("추가량") },
                    style =TextFieldStyle(
                        value = stepAmount,
                        onValueChange = { if (it.all { char -> char.isDigit() }) onStepAmountChange(it) },
                        placeholder = "200",
                        suffix = unitLabel.take(3),
                        keyboardType = KeyboardType.Number
                ))
            }
        }
        if (stepAmount.isNotEmpty() && unitLabel.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.tiny)
            ) {
                Icon(Icons.Default.Lightbulb, null, tint = AppTheme.colors.mainColor, modifier = Modifier.size(
                    AppTheme.dimens.large))
                BodyTextSmall(
                    text = "버튼을 한 번 누를 때마다 ${stepAmount}${unitLabel}씩 채워집니다.",
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview_DailySettingForm(){
    DailySettingForm(
        totalTarget = "2000",
        onTotalTargetChange = {},
        stepAmount = "2",
        onStepAmountChange = {},
        unitLabel = "d",
    ) { }
}