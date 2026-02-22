package com.hye.healthpossible.ui.screen

import OnboardingStep3_Survey
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.healthpossible.ui.component.onboard.OnboardingStep1
import com.hye.healthpossible.ui.component.onboard.OnboardingStep2
import com.hye.healthpossible.ui.viewmodel.OnboardingViewModel
import com.hye.shared.base.BaseScreenTemplate
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onOnboardingFinished: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { 3 }) //Todo. 실제 페이지 수를 따로 자동화 하게 하기 + 상태로 관리
    val scope = rememberCoroutineScope()

    // 🔥 ViewModel에서 가입 성공 신호가 오면 페이지 1(두 번째 단계)로 이동
    LaunchedEffect(uiState.navigateToNextStep) {
        if (uiState.navigateToNextStep) {
            pagerState.animateScrollToPage(1)
            viewModel.onNavigatedToNextStep() // 상태 초기화
        }
    }
    BaseScreenTemplate(
        viewModel = viewModel,
        screenName = "OnboardingScreen",
        isLoading = uiState.isLoading,
        errorMessage = uiState.error,
    ) {

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
                        onDebouncedCodenameChange = { viewModel.checkCodenameDuplication() },
                        onNext = { viewModel.signUpGuest() }
                    )

                    1 -> OnboardingStep2(
                        onSelfSelect = onOnboardingFinished,
                        currentSelection = uiState.selectionType,
                        changeSelection = viewModel::updateSelectionType,
                        onAiSelect = {
                            scope.launch { pagerState.animateScrollToPage(2) }
                        },
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
