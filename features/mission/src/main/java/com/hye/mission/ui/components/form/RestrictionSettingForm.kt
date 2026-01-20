package com.hye.mission.ui.components.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hye.domain.model.mission.RestrictionType
import com.hye.shared.components.ui.StyledTextField
import com.hye.shared.components.ui.TextFieldStyle
import com.hye.shared.theme.AppTheme


@Composable
fun RestrictionSettingForm(
    type: RestrictionType,
    onTypeSelected: (RestrictionType) -> Unit,
    maxAllowedTime: String,
    onMaxTimeChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraLarge)) {
        Text("제한 방식", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.textSecondary, fontWeight = FontWeight.Bold)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RestrictionType.values().forEach { rType ->
                val isSelected = type == rType
                Button(
                    onClick = { onTypeSelected(rType) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) AppTheme.colors.mainColor else AppTheme.colors.backgroundMuted,
                        contentColor = if (isSelected) Color.White else AppTheme.colors.textSecondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = if (rType == RestrictionType.TIMER) "타이머 (시간 참기)" else "체크 (하루 금지)",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        AnimatedVisibility(visible = type == RestrictionType.TIMER) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("목표 유지 시간", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.textSecondary, fontWeight = FontWeight.Bold)
                StyledTextField(TextFieldStyle(
                    value = maxAllowedTime,
                    onValueChange = { if (it.all { char -> char.isDigit() }) onMaxTimeChange(it) },
                    placeholder = "예: 16",
                    suffix = "시간",
                    keyboardType = KeyboardType.Number
                ))
                Text(
                    text = "설정한 시간 동안 타이머가 돌아갑니다. (예: 간헐적 단식)",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.textSecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        if (type == RestrictionType.CHECK) {
            Surface(
                color = AppTheme.colors.backgroundMuted,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "하루 종일 해당 행동을 하지 않았는지 밤에 체크합니다.\n(예: 야식 금지, 금주)",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.textSecondary,
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 20.sp
                )
            }
        }
    }
}
