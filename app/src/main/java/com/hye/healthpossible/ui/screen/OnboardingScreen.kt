package com.hye.healthpossible.ui.screen

import OnboardingStep2_PathSelection
import OnboardingStep3_Survey
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.healthpossible.ui.component.onboard.OnboardingStep1
import com.hye.healthpossible.ui.viewmodel.OnboardingViewModel
import com.hye.shared.base.BaseScreenTemplate
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onOnboardingFinished: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreenTemplate(
        viewModel = viewModel,
        screenName = "OnboardingScreen",
        isLoading = uiState.isLoading,
        errorMessage = uiState.error,
    ) {
        val pagerState = rememberPagerState(pageCount = { 3 }) //Todo. 실제 페이지 수를 따로 자동화 하게 하기
        val scope = rememberCoroutineScope()

        var isSurveySelected by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> OnboardingStep1(
                        codename = uiState.codename,
                        isChecking = uiState.isCheckingCodename,
                        isValid = uiState.isCodenameValid,
                        errorMessage = uiState.codenameErrorMessage,
                        onCodenameChange = viewModel::updateCodename,
                        onDebouncedCodenameChange = { viewModel::checkCodenameDuplication },
                        onNext = { scope.launch { pagerState.animateScrollToPage(1) } }
                    )

                    1 -> OnboardingStep2_PathSelection(
                        onManualSelect = onOnboardingFinished,
                        onAiSelect = {
                            isSurveySelected = true
                            scope.launch { pagerState.animateScrollToPage(2) }
                        },
                        onBack = { scope.launch { pagerState.animateScrollToPage(0) } }
                    )

                    2 -> OnboardingStep3_Survey(
                        onSurveyComplete = onOnboardingFinished,
                        onBack = { scope.launch { pagerState.animateScrollToPage(1) } }
                    )
                }
            }
        }
    }
}
