package com.hye.healthpossible.ui.component.onboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.hye.healthpossible.R
import com.hye.healthpossible.ui.state.SelectionType
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.ui.common.selectionBorderStroke
import com.hye.shared.ui.common.selectionBtnColor
import com.hye.shared.ui.common.selectionContentColor
import com.hye.shared.ui.text.LabelText
import com.hye.shared.util.text


@Composable
@Preview
fun OnboardingStep2_Preview() {
    OnboardingStep2(
        onSelfSelect = {},
        onAiSelect = {},
        currentSelection = SelectionType.NONE,
    )
}

//Todo: Text 정리 할 것
@Composable
fun OnboardingStep2(
    onSelfSelect: () -> Unit,
    onAiSelect: () -> Unit,
    currentSelection: SelectionType,
    changeSelection: (SelectionType) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .padding(top = AppTheme.dimens.xxxxl, start = AppTheme.dimens.xxl, end = AppTheme.dimens.xxl),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.md)
        ) {
            LabelText(
                text = R.string.onboarding_step2_strategy_briefing.text,
                lineHeight = AppTheme.dimens.l.toSp,
                color = AppTheme.colors.mainColor,
            )
            Text(
                text = R.string.onboarding_step2_title.text,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textPrimary,
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimens.xxl, vertical = AppTheme.dimens.xxl)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.md)
        ) {
            SelectionBigCard(
                title = R.string.onboarding_step2_ai_card_title.text,
                description = R.string.onboarding_step2_ai_card_description.text,
                actionText = R.string.onboarding_step2_ai_card_action.text,
                isCurrent = SelectionType.AI.isCurrent(currentSelection),
                onClick = SelectionType.AI.getClickAction(currentSelection, onAiSelect, changeSelection)
            )

            SelectionBigCard(
                title = R.string.onboarding_step2_manual_card_title.text,
                description = R.string.onboarding_step2_manual_card_description.text,
                actionText = R.string.onboarding_step2_manual_card_action.text,
                isCurrent = SelectionType.SELF.isCurrent(currentSelection),
                onClick = SelectionType.SELF.getClickAction(currentSelection, onSelfSelect, changeSelection)
            )
        }

        Text(
            text = R.string.onboarding_step2_notice.text,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.colors.textSecondary,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = AppTheme.dimens.xxl, vertical = AppTheme.dimens.xxxxl)
        )
    }
}

private fun SelectionType.isCurrent(currentSelection: SelectionType) : Boolean =
    this == currentSelection

private fun SelectionType.getClickAction(
    currentSelection: SelectionType,
    onNext: () -> Unit,
    onChange: (SelectionType) -> Unit
): () -> Unit = {
    if (this == currentSelection) onNext() else onChange(this)
}


@Composable
fun SelectionBigCard(
    title: String,
    description: String,
    actionText: String,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    val containerColor = isCurrent.selectionBtnColor()
    val contentColor = isCurrent.selectionContentColor()
    val border = isCurrent.selectionBorderStroke(color = AppTheme.colors.uncheckedTrackColor)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(AppTheme.dimens.xxl),
        color = containerColor,
        border = border
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.dimens.xxl),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.8f),
                lineHeight = AppTheme.dimens.l.toSp
            )

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = AppTheme.dimens.s),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier
                        .padding(start = AppTheme.dimens.xxs)
                        .size(AppTheme.dimens.xxl)
                )
            }
        }
    }
}
