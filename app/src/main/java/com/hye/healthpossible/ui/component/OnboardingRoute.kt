package com.hye.healthpossible.ui.component

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.auth.model.AuthViewModel
import com.hye.auth.ui.screen.CodenameSetup
import com.hye.healthpossible.ui.screen.OnboardingScreen
import com.hye.profile.model.ProfileViewModel
import com.hye.profile.ui.StrategySelection
import com.hye.profile.ui.HealthSurvey
import com.hye.shared.base.BaseScreenTemplate
import kotlinx.coroutines.launch

@Composable
fun OnboardingRoute(
    onOnboardingFinished: () -> Unit,
    onNavigateBack: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    OnboardingScreen(
        pagerState = pagerState,
        authSlot = {
            val authState by authViewModel.uiState.collectAsStateWithLifecycle()

            BaseScreenTemplate(
                viewModel = authViewModel,
                screenName = "CodenameSetup",
                isLoading = authState.isLoading,
                errorMessage = authState.error, // AuthUiState의 에러 사용
                onNavigateBack = onNavigateBack
            ) {
                CodenameSetup(
                    codename = authState.codename,
                    isChecking = authState.isCheckingCodename,
                    isValid = authState.isCodenameValid,
                    errorMessage = authState.codenameErrorMessage,
                    onCodenameChange = authViewModel::updateCodename,
                    onDebouncedCodenameChange = { authViewModel.checkCodenameDuplication() },
                    onKakaoLoginClick = { /* 카카오 연동 */ },
                    onNextClick = {
                        authViewModel.signUpGuest(
                            onSuccess = { scope.launch { pagerState.animateScrollToPage(1) } }
                        )
                    }
                )
            }
        },
        strategySlot = {
            val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()

            BaseScreenTemplate(
                viewModel = profileViewModel,
                screenName = "StrategySelection",
                isLoading = profileState.isLoading,
                errorMessage = profileState.error
            ) {
                StrategySelection(
                    currentSelection = profileState.selectionType,
                    changeSelection = profileViewModel::updateSelectionType,
                    onSelfSelect = onOnboardingFinished,
                    onAiSelect = { scope.launch { pagerState.animateScrollToPage(2) } }
                )
            }
        },
        surveySlot = {
            val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()

            BaseScreenTemplate(
                viewModel = profileViewModel,
                screenName = "HealthSurvey",
                isLoading = profileState.isLoading,
                errorMessage = profileState.error
            ) {
                HealthSurvey(
                    questions = profileState.surveyQuestions,
                    selectedAnswers = profileState.surveyAnswers,
                    onAnswerToggled = profileViewModel::toggleSurveyAnswer,
                    onComplete = { profileViewModel.saveProfileData(onSuccess = onOnboardingFinished) },
                    onBack = { scope.launch { pagerState.animateScrollToPage(1) } }
                )
            }
        }
    )
}