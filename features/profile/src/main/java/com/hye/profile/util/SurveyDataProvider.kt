package com.hye.profile.util

import androidx.compose.runtime.Composable
import com.hye.domain.model.survey.SurveyQuestion
import com.hye.features.profile.R
import com.hye.shared.util.text

object SurveyDataProvider {
    val questions : List<SurveyQuestion>
        @Composable
        get() = listOf(
            SurveyQuestion(
                id = "Q1",
                tag = R.string.onboarding_step3_q1_tag.text,
                title = R.string.onboarding_step3_q1_title.text,
                isMultiSelect = true,
                options = listOf(
                    R.string.onboarding_step3_q1_option1.text,
                    R.string.onboarding_step3_q1_option2.text,
                    R.string.onboarding_step3_q1_option3.text,
                    R.string.onboarding_step3_q1_option4.text,
                    R.string.onboarding_step3_q1_option5.text
                )
            ),
            SurveyQuestion(
                id = "Q2",
                tag = R.string.onboarding_step3_q2_tag.text,
                title = R.string.onboarding_step3_q2_title.text,
                isMultiSelect = true,
                options = listOf(
                    R.string.onboarding_step3_q2_option1.text,
                    R.string.onboarding_step3_q2_option2.text,
                    R.string.onboarding_step3_q2_option3.text,
                    R.string.onboarding_step3_q2_option_none_healthy.text
                )
            ),
            SurveyQuestion(
                id = "Q3",
                tag = R.string.onboarding_step3_q3_tag.text,
                title = R.string.onboarding_step3_q3_title.text,
                isMultiSelect = true,
                options = listOf(
                    R.string.onboarding_step3_q3_option1.text,
                    R.string.onboarding_step3_q3_option2.text,
                    R.string.onboarding_step3_q3_option3.text,
                    R.string.onboarding_step3_q3_option4.text
                )
            ),
            SurveyQuestion(
                id = "Q4",
                tag = R.string.onboarding_step3_q4_tag.text,
                title = R.string.onboarding_step3_q4_title.text,
                isMultiSelect = false,
                options = listOf(
                    R.string.onboarding_step3_q4_option1.text,
                    R.string.onboarding_step3_q4_option2.text,
                    R.string.onboarding_step3_q4_option3.text
                )
            )
        )

}