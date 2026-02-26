package com.hye.profile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.hye.features.profile.R
import com.hye.profile.ui.components.survey.SurveyPage
import com.hye.profile.ui.components.survey.SurveyProgressIndicator
import com.hye.profile.util.SurveyDataProvider
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.ui.button.BackButton
import com.hye.shared.util.text
import kotlinx.coroutines.launch


@Preview
@Composable
fun PreviewHealthSurvey() {
    HealthSurvey(
        selectedAnswers = emptyMap(),
        onAnswerToggled = { _, _, _ -> },
        onComplete = { },
        onBack = { },
    )
}


@Composable
fun HealthSurvey(
    selectedAnswers: Map<String, Set<String>>,
    onAnswerToggled: (questionId: String, option: String, isMultiSelect: Boolean) -> Unit,
    onComplete: () -> Unit,
    onBack: () -> Unit,
    completeButtonText: String = R.string.onboarding_step3_complete_button.text
) {

    val questions = SurveyDataProvider.questions

    val pagerState = rememberPagerState(pageCount = { questions.size })
    val scope = rememberCoroutineScope()

    val completionStatus = questions.map { question ->
        selectedAnswers[question.id]?.isNotEmpty() == true
    }
    val isAllCompleted = completionStatus.all { it } && questions.isNotEmpty()
    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(top = AppTheme.dimens.md)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimens.xxl, vertical = AppTheme.dimens.md),
                    contentAlignment = Alignment.Center
                ) {
                    if (pagerState.currentPage == 0) BackButton(onBack = onBack, applyShadow = false, modifier = Modifier.align(Alignment.CenterStart))

                    SurveyProgressIndicator(
                        totalSteps = questions.size,
                        currentStep = pagerState.currentPage,
                        completionStatus = completionStatus,
                        onStepClick = { index ->
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }
                    )
                }
            }
        },
        containerColor = AppTheme.colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = AppTheme.dimens.xxl),
                pageSpacing = AppTheme.dimens.md,
                verticalAlignment = Alignment.Top
            ) { page ->
                val question = questions[page]
                val currentSelection = selectedAnswers[question.id] ?: emptySet()

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                ) {
                    SurveyPage(
                        question = question,
                        selectedOptions = currentSelection,
                        onOptionClick = { option -> onAnswerToggled(question.id, option, question.isMultiSelect) }
                    )
                }
            }

            AnimatedVisibility(
                visible = pagerState.currentPage == questions.size - 1,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.xxl)
                        .navigationBarsPadding()
                ) {
                    Button(
                        onClick = onComplete,
                        enabled = isAllCompleted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppTheme.dimens.nextButton)
                            .shadow(
                                if (isAllCompleted) AppTheme.dimens.xxs else AppTheme.dimens.zero,
                                RoundedCornerShape(AppTheme.dimens.md)
                            ),
                        shape = RoundedCornerShape(AppTheme.dimens.md),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.mainColor,
                            disabledContainerColor = AppTheme.colors.mainColorLight
                        )
                    ) {
                        Text(completeButtonText, fontSize = AppTheme.dimens.md.toSp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}