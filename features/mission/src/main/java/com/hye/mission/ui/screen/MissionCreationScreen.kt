package com.hye.mission.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.domain.model.mission.MissionActionsByType
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.DayOfWeek
import com.hye.domain.model.mission.types.MissionType
import com.hye.domain.model.mission.types.type
import com.hye.features.mission.R
import com.hye.mission.ui.components.form.MissionForm
import com.hye.mission.ui.components.form.common.CategorySelectionTab
import com.hye.mission.ui.components.form.common.CommonInputSection
import com.hye.mission.ui.components.form.type.exercise.ExerciseSettingFormBottomSeet
import com.hye.mission.ui.components.mission.MissionCreationContent
import com.hye.mission.ui.viewmodel.MissionCreationViewModel
import com.hye.mission.ui.state.MissionState
import com.hye.mission.ui.util.SettingRules.ANIMATION_SPEC
import com.hye.mission.ui.util.getDesign
import com.hye.shared.base.BaseScreenTemplate
import com.hye.shared.mock.MissionMockData
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.common.IconStyle
import com.hye.shared.ui.common.Orientation
import com.hye.shared.ui.common.enabledColor
import com.hye.shared.ui.common.selectionContentColor
import com.hye.shared.ui.menu.MenuStyle
import com.hye.shared.ui.menu.StyledMenu
import com.hye.shared.ui.text.MenuLabel
import com.hye.shared.ui.text.StyledInputSection
import com.hye.shared.ui.text.TitleMedium
import com.hye.shared.ui.text.StyledInputField
import com.hye.shared.util.TopBarAction
import com.hye.shared.util.text
import com.hye.shared.R as commonR


@Composable
fun MissionCreationScreen(
    viewModel: MissionCreationViewModel = hiltViewModel(),
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiStatus.collectAsStateWithLifecycle()
    val inputMission = uiState.inputMission

    val actions = remember(viewModel) {
        MissionActionsByType(
            onExerciseUnitChange = viewModel::updateExerciseUnit,
            onExerciseTargetChange = viewModel::updateExerciseTarget,
            onExerciseSupportAgentToggle = viewModel::toggleExerciseSupportAgent,
            onChangeBottomSheetState = viewModel::toggleBottomSheet,
            onSelectExerciseType = viewModel::selectExerciseType,
            onDietMethodChange = viewModel::updateDietRecordMethod,
            onRoutineUnitLabelChange = viewModel::updateRoutineUnitLabel,
            onRoutineTotalChange = viewModel::updateRoutineTotalTarget,
            onRoutineStepChange = viewModel::updateRoutineStepAmount,
            onRestrictionTypeChange = viewModel::updateRestrictionType,
            onRestrictionTimeChange = viewModel::updateRestrictionTime,
        )
    }

    BaseScreenTemplate(
        screenName = R.string.mission_creation_screen.text,
        viewModel = viewModel,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        setTopBarActions = setTopBarActions,
        onNavigateBack = onNavigateBack,
        topBarActionContent = {
            val isTitleValid = inputMission != null && inputMission.title.isNotBlank()
            TopBarAction(
                onClick = { viewModel.insertMission() },
                enabled = isTitleValid,
                label = commonR.string.save.text,
                color = isTitleValid.enabledColor()
            )
        }
    ) {
        MissionCreationScreenBody(
            uiState = uiState,
            actions = actions,
            onTitleChange = viewModel::updateTitle,
            onDayToggle = viewModel::toggleDay,
            onCategorySelected = viewModel::startCreation,
            onMemoChange = viewModel::updateMemo,
            onDismissBottomSheet = { viewModel.toggleBottomSheet(false) },
            onSelectExerciseType = viewModel::selectExerciseType
        )
    }
}
@Composable
fun MissionCreationScreenBody(
    uiState: MissionState,
    actions: MissionActionsByType,
    onTitleChange: (String) -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCategorySelected: (MissionType) -> Unit,
    onMemoChange: (String) -> Unit,
    onDismissBottomSheet: (Boolean) -> Unit,
    onSelectExerciseType: (AiExerciseType) -> Unit
) {
    val inputMission = uiState.inputMission

    Box(modifier = Modifier.fillMaxSize()) {

        MissionCreationContent(
            // 1. 상단 공통 입력
            commonInputContent = {
                if (inputMission != null) {
                    CommonInputSection(
                        name = inputMission.title,
                        onNameChange = onTitleChange,
                        selectedDays = inputMission.days,
                        onDayToggle = onDayToggle
                    )
                }
            },

            // 2. 카테고리 탭
            categoryTabContent = {
                if (inputMission != null) {
                    CategorySelectionTab(
                        selectedCategory = inputMission.type,
                        onCategorySelected = onCategorySelected,
                        itemContent = { category, isSelected ->
                            StyledMenu(
                                MenuStyle(
                                    modifier = Modifier.padding(
                                        horizontal = AppTheme.dimens.l,
                                        vertical = AppTheme.dimens.s
                                    ),
                                    icon = IconStyle(
                                        image = category.getDesign().icon,
                                        tint = isSelected.selectionContentColor(),
                                        size = AppTheme.dimens.l
                                    ),
                                    spacedBy = AppTheme.dimens.xxs
                                ),
                                content = {
                                    MenuLabel(
                                        category.label,
                                        color = isSelected.selectionContentColor()
                                    )
                                },
                                orientation = Orientation.Row
                            )
                        }
                    )
                }
            },

            // 3. 상세 폼
            detailedFormContent = {
                if (inputMission != null) {
                    AnimatedContent(
                        targetState = inputMission,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(ANIMATION_SPEC)) togetherWith fadeOut(
                                animationSpec = tween(ANIMATION_SPEC)
                            )
                        },
                        contentKey = { it.type },
                        label = "MissionFormAnimation"
                    ) { mission ->
                        MissionForm(
                            mission = mission,
                            actions = actions,
                            selectedExerciseName = uiState.selectedExerciseType
                        )
                    }
                }
            },

            // 4. 메모 입력
            tagInputContent = {
                if (inputMission != null) {
                    StyledInputSection(
                        label = { TitleMedium(R.string.mission_plan_memo_placeholder.text) },
                        text = {
                            StyledInputField(
                                value = inputMission.memo ?: "", // memo null 처리 필요할 수 있음
                                onValueChange = onMemoChange,
                            )
                        }
                    )
                }
            },
        )

        // 바텀 시트
        AnimatedVisibility(
            visible = uiState.isBottomSheetOpen,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(600)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(600)
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ExerciseSettingFormBottomSeet(
                onDismissRequest = onDismissBottomSheet,
                selectedExerciseType = uiState.selectedExerciseType,
                itemOnclick = onSelectExerciseType
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation")
@Preview(showBackground = true)
@Composable
fun Preview_MissionCreationScreen() {
    val dummyMission = MissionMockData.getMockMissions()[0].mission

    val dummyState = MissionState(
        inputMission = dummyMission,
        isBottomSheetOpen = false
    )

    val dummyActions = MissionActionsByType(
        {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}
    )

        MissionCreationScreenBody(
            uiState = dummyState,
            actions = dummyActions,
            onTitleChange = {},
            onDayToggle = {},
            onCategorySelected = {},
            onMemoChange = {},
            onDismissBottomSheet = {},
            onSelectExerciseType = {}
        )
}