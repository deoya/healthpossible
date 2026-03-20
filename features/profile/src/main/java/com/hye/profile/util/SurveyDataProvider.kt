package com.hye.profile.util

import androidx.compose.runtime.Composable
import com.hye.domain.model.survey.SurveyQuestion
import com.hye.domain.model.survey.SurveyQuestionId
import com.hye.features.profile.R
import com.hye.shared.util.text

object SurveyDataProvider {
    val questions: List<SurveyQuestion>
        @Composable
        get() = listOf(
            SurveyQuestion(
                id = SurveyQuestionId.HEALTH_GOAL,
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
                id = SurveyQuestionId.PAIN_POINT,
                tag = R.string.onboarding_step3_q2_tag.text,
                title = R.string.onboarding_step3_q2_title.text,
                isMultiSelect = true,
                options = listOf(
                    R.string.onboarding_step3_q2_option1.text,
                    R.string.onboarding_step3_q2_option2.text,
                    R.string.onboarding_step3_q2_option3.text,
                    R.string.onboarding_step3_q2_option_none_healthy.text
                ),
                exclusiveOption = R.string.onboarding_step3_q2_option_none_healthy.text
            ),
            SurveyQuestion(
                id = SurveyQuestionId.BAD_HABIT,
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
                id = SurveyQuestionId.ACTIVITY_LEVEL,
                tag = R.string.onboarding_step3_q4_tag.text,
                title = R.string.onboarding_step3_q4_title.text,
                isMultiSelect = false,
                options = listOf(
                    R.string.onboarding_step3_q4_option1.text,
                    R.string.onboarding_step3_q4_option2.text,
                    R.string.onboarding_step3_q4_option3.text
                )
            ),
            SurveyQuestion(
                id = SurveyQuestionId.CHRONIC_DISEASE,
                tag = R.string.onboarding_step3_q5_tag.text,
                title = R.string.onboarding_step3_q5_title.text,
                isMultiSelect = true,
                options = listOf(
                    R.string.onboarding_step3_q5_option1.text, // 당뇨병
                    R.string.onboarding_step3_q5_option2.text, // 고혈압
                    R.string.onboarding_step3_q5_option3.text, // 고지혈증
                    R.string.onboarding_step3_q5_option5.text, // 관절염
                    R.string.onboarding_step3_q5_option6.text, // 갑상선 질환
                    R.string.onboarding_step3_q5_option7.text, // 빈혈
                    R.string.onboarding_step3_q5_option8.text, // 비만
                    R.string.onboarding_step3_q5_option_none.text // 특별히 없음
                ),
                // '특별히 없음' 선택 시 다른 질환들 일괄 선택 해제
                exclusiveOption = R.string.onboarding_step3_q5_option_none.text
            )
        )
}