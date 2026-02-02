package com.hye.mission.ui.components.form.type

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.hye.domain.model.mission.types.RestrictionType
import com.hye.shared.ui.text.TextFieldStyle
import com.hye.shared.theme.AppTheme
import com.hye.features.mission.R
import com.hye.mission.ui.util.choiceRule
import com.hye.shared.ui.text.TextDescription
import com.hye.shared.ui.text.LabelMedium
import com.hye.shared.ui.button.StyledButton
import com.hye.shared.ui.text.StyledInputSection
import com.hye.shared.ui.common.selectionBtnColor
import com.hye.shared.ui.common.selectionContentColor
import com.hye.shared.theme.toSp
import com.hye.shared.util.text


@Composable
fun RestrictionSettingForm(
    type: RestrictionType,
    onTypeSelected: (RestrictionType) -> Unit,
    maxAllowedTime: String,
    onMaxTimeChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.l)) {
        LabelMedium(R.string.mission_plan_limit_type.text)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)) {
            RestrictionType.values().forEach { rType ->
                val isSelected = type == rType
                StyledButton(
                    onClick = { onTypeSelected(rType) },
                    containerColor = isSelected.selectionBtnColor(deselection = AppTheme.colors.backgroundMuted),
                    contentColor = isSelected.selectionContentColor(),
                    modifier = Modifier
                        .weight(1f)
                        .height(AppTheme.dimens.bigBtn),
                ){
                    Text(text = rType.choiceRule.text)
                }
            }
        }
        // when을 밖으로 꺼내는 것이 아니라, 애니메이션 컨테이너가 when을 감싸야 합니다.
        AnimatedContent(
            targetState = type
        ) { targetType ->
            when (targetType) {
                RestrictionType.TIMER -> {
                    StyledInputSection(
                        { LabelMedium(R.string.mission_plan_goal_time.text) },
                        style = TextFieldStyle(
                            value = maxAllowedTime,
                            onValueChange = { if (it.all { char -> char.isDigit() }) onMaxTimeChange(it) },
                            placeholder = R.string.mission_plan_goal_time_placeholder.text,
                            suffix = R.string.mission_unit_times.text,
                            keyboardType = KeyboardType.Number
                        ),
                        description = {
                            TextDescription(text = R.string.mission_plan_goal_time_description.text)
                        }
                    )
                }
                RestrictionType.CHECK -> {
                    Surface(
                        color = AppTheme.colors.backgroundMuted,
                        shape = RoundedCornerShape(AppTheme.dimens.s),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextDescription(
                            text = R.string.mission_plan_goal_count_description.text,
                            modifier = Modifier.padding(AppTheme.dimens.md),
                            lineHeight = AppTheme.dimens.l.toSp
                        )
                    }
                }
            }
        }
    }
}

// 1. 실제로 상호작용 가능한 프리뷰 (버튼 클릭 시 UI 변경 확인용)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewRestrictionSettingForm_Interactive() {
    // AppTheme { // <- 실제 프로젝트의 테마로 감싸주세요 (폰트, 색상 적용 위함)

    // 프리뷰 내부에서 상태 관리
    var currentType by remember { mutableStateOf(RestrictionType.TIMER) }
    var timeInput by remember { mutableStateOf("") }

    Box(modifier = Modifier.padding(AppTheme.dimens.md)) {
        RestrictionSettingForm(
            type = currentType,
            onTypeSelected = { newType -> currentType = newType },
            maxAllowedTime = timeInput,
            onMaxTimeChange = { newValue -> timeInput = newValue }
        )
    }
    // }
}

// 2. '타이머' 타입일 때의 모습 고정 프리뷰
@Preview(showBackground = true)
@Composable
fun PreviewRestrictionSettingForm_TimerMode() {
    Box(modifier = Modifier.padding(AppTheme.dimens.md)) {
        RestrictionSettingForm(
            type = RestrictionType.TIMER,
            onTypeSelected = {},
            maxAllowedTime = "16",
            onMaxTimeChange = {}
        )
    }
}

// 3. '체크' 타입일 때의 모습 고정 프리뷰
@Preview(showBackground = true)
@Composable
fun PreviewRestrictionSettingForm_CheckMode() {
    Box(modifier = Modifier.padding(AppTheme.dimens.md)) {
        RestrictionSettingForm(
            type = RestrictionType.CHECK,
            onTypeSelected = {},
            maxAllowedTime = "",
            onMaxTimeChange = {}
        )
    }
}
