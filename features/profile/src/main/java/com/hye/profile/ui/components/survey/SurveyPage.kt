package com.hye.profile.ui.components.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hye.domain.model.survey.SurveyQuestion
import com.hye.features.profile.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.card.ModernSelectionCard
import com.hye.shared.ui.text.LabelText
import com.hye.shared.ui.text.SectionText
import com.hye.shared.ui.text.TextStyleSize
import com.hye.shared.util.text

// Todo : 공통 모듈로 UI 추출
@Composable
fun SurveyPage(
    question: SurveyQuestion,
    selectedOptions: Set<String>,
    onOptionClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(top = AppTheme.dimens.xxl)) {
        LabelText(question.tag, style = TextStyleSize.Large, color = AppTheme.colors.mainColor)
        SectionText(question.title, style = TextStyleSize.Small)

        if (question.isMultiSelect) {
            Text(R.string.onboarding_step3_multi_select_caption.text(), style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.textSecondary, modifier = Modifier.padding(top = AppTheme.dimens.xxs))
        }

        Column(
            modifier = Modifier.padding(top = AppTheme.dimens.xxxxl),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)
        ) {
            question.options.forEach { option ->
                val isSelected = selectedOptions.contains(option)
                ModernSelectionCard(
                    text = option,
                    isSelected = isSelected,
                    onClick = { onOptionClick(option) }
                )
            }
        }
    }
}