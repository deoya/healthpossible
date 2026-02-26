package com.hye.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.hye.domain.model.survey.SelectionType
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.ui.card.SelectionBigCard
import com.hye.shared.ui.text.LabelText
import com.hye.shared.util.text
import com.hye.features.profile.R

@Composable
fun StrategySelection(
    currentSelection: SelectionType,
    changeSelection: (SelectionType) -> Unit,
    onSelfSelect: () -> Unit,
    onAiSelect: () -> Unit
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

private fun SelectionType.isCurrent(currentSelection: SelectionType): Boolean =
    this == currentSelection

private fun SelectionType.getClickAction(
    currentSelection: SelectionType,
    onNext: () -> Unit,
    onChange: (SelectionType) -> Unit
): () -> Unit = {
    if (this == currentSelection) onNext() else onChange(this)
}
