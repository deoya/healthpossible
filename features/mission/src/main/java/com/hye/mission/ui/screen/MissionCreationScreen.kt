package com.hye.mission.ui.screen

import ToastHelper
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.domain.model.mission.MissionActionsByType
import com.hye.domain.model.mission.types.DayOfWeek
import com.hye.domain.model.mission.types.ExerciseMission
import com.hye.domain.model.mission.types.ExerciseUnit
import com.hye.domain.model.mission.types.type
import com.hye.mission.ui.components.form.CategorySelectionTab
import com.hye.mission.ui.components.form.CommonInputSection
import com.hye.mission.ui.components.form.MissionForm
import com.hye.mission.ui.components.form.TagInputSection
import com.hye.mission.ui.components.mission.MissionCreationContent
import com.hye.mission.ui.model.MissionCreationViewModel
import com.hye.mission.ui.model.MissionState
import com.hye.mission.ui.util.SettingRules.ANIMATION_SPEC
import com.hye.mission.ui.util.getDesign
import com.hye.shared.util.TopBarAction
import com.hye.shared.R
import com.hye.shared.components.ui.MenuLabel
import com.hye.shared.components.ui.MenuStyle
import com.hye.shared.components.ui.StyledMenu
import com.hye.shared.components.ui.common.IconStyle
import com.hye.shared.components.ui.common.Orientation
import com.hye.shared.components.ui.common.enabledColor
import com.hye.shared.components.ui.common.selectionBtnContentColor
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.text

@Composable
fun MissionCreationScreen(
    viewModel: MissionCreationViewModel = hiltViewModel(),
    setTopBarActions: ((@Composable () -> Unit)?) -> Unit,
) {
    val uiState by viewModel.uiStatus.collectAsStateWithLifecycle()
    var inputMission =  uiState.inputMission

    val actions = remember(viewModel) {
        MissionActionsByType(
            //운동
            onExerciseUnitChange = viewModel::updateExerciseUnit,
            onExerciseTargetChange = viewModel::updateExerciseTarget,
            onExerciseSupportAgentToggle = viewModel::toggleExerciseSupportAgent,
            // 식단
            onDietMethodChange = viewModel::updateDietRecordMethod,
            // 루틴
            onRoutineUnitLabelChange = viewModel::updateRoutineUnitLabel,
            onRoutineTotalChange = viewModel::updateRoutineTotalTarget,
            onRoutineStepChange = viewModel::updateRoutineStepAmount,
            // 제한
            onRestrictionTypeChange = viewModel::updateRestrictionType,
            onRestrictionTimeChange = viewModel::updateRestrictionTime,
        )
    }
    DisposableEffect(inputMission?.title) {

        val isTitleValid = inputMission != null && inputMission.title.isNotBlank()

        setTopBarActions({
            TopBarAction(
                onClick = { viewModel.insertMission() },
                enabled = isTitleValid,
                label = R.string.save.text,
                color = isTitleValid.enabledColor()
            )
        })
        onDispose { setTopBarActions(null) }
    }

    LaunchedEffect(uiState.isInserted) {
        if(uiState.isInserted){
            ToastHelper.show(uiState.userMessage.toString())
            viewModel.resetMissionState()
        }
    }

    MissionCreationContent(
        // 1. 상단 공통 입력
        commonInputContent = {
            if (inputMission != null) {
                CommonInputSection(
                    name = inputMission.title,
                    onNameChange = viewModel::updateTitle,
                    selectedDays = inputMission.days,
                    onDayToggle = viewModel::toggleDay
                )
            }
        },

        // 2. 카테고리 탭
        categoryTabContent = {
            if (inputMission != null) {
                CategorySelectionTab(
                    selectedCategory = inputMission.type,
                    onCategorySelected = viewModel::startCreation,
                    itemContent = { category, isSelected ->
                        StyledMenu(
                            MenuStyle(
                                modifier = Modifier.padding(
                                    horizontal = AppTheme.dimens.l,
                                    vertical = AppTheme.dimens.s
                                ),
                                icon = IconStyle(
                                    image = category.getDesign().icon,
                                    tint = isSelected.selectionBtnContentColor(),
                                    size = AppTheme.dimens.l
                                ),
                                spacedBy = AppTheme.dimens.xxs
                            ),
                            content = { MenuLabel(category.label, color = isSelected.selectionBtnContentColor()) },
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
                        fadeIn(animationSpec = tween(ANIMATION_SPEC)) togetherWith fadeOut(animationSpec = tween(ANIMATION_SPEC))
                    },
                    contentKey = { it.type },
                ) { mission ->
                    MissionForm(mission = mission, actions = actions)
                }
            }
        },

        // 4. 태그 입력
        tagInputContent = {
            if (inputMission != null) {
                var currentTagInput by remember { mutableStateOf("") }

                TagInputSection(
                    tags = inputMission.tags,
                    inputValue = currentTagInput,
                    onValueChange = { currentTagInput = it },
                    onAddTag = {
                        viewModel.addTag(currentTagInput)
                        currentTagInput = ""
                    },
                    onRemoveTag = viewModel::removeTag
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview_MissionCreationContent_LayoutOnly() {
    MissionCreationContent(
        commonInputContent = { Text("공통 입력 영역") },
        categoryTabContent = { Text("탭 영역") },
        detailedFormContent = { Text("폼 영역") },
        tagInputContent = { Text("태그 영역") }
    )
}