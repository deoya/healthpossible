package com.hye.mission.ui.components.mission

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hye.domain.model.mission.DayOfWeek
import com.hye.domain.model.mission.DietMission
import com.hye.domain.model.mission.DietRecordMethod
import com.hye.domain.model.mission.ExerciseMission
import com.hye.domain.model.mission.ExerciseUnit
import com.hye.domain.model.mission.MissionCategory
import com.hye.domain.model.mission.RestrictionMission
import com.hye.domain.model.mission.RestrictionType
import com.hye.domain.model.mission.RoutineMission
import com.hye.mission.ui.components.form.CategorySelectionTab
import com.hye.mission.ui.components.form.CommonInputSection
import com.hye.mission.ui.components.form.DailySettingForm
import com.hye.mission.ui.components.form.DietSettingForm
import com.hye.mission.ui.components.form.ExerciseSettingForm
import com.hye.mission.ui.components.form.RestrictionSettingForm
import com.hye.mission.ui.components.form.TagInputSection
import com.hye.mission.ui.model.MissionState
import com.hye.shared.components.ui.StyledCard
import com.hye.shared.theme.AppTheme

@Composable
fun MissionCreationContent(
    uiState: MissionState,

    onTitleChange: (String) -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onCategorySelected: (MissionCategory) -> Unit,
    onExerciseUnitChange: (ExerciseUnit) -> Unit,
    onExerciseTargetChange: (String) -> Unit,
    onExerciseTimerToggle: (Boolean) -> Unit,
    onDietMethodChange: (DietRecordMethod) -> Unit,
    onRoutineUnitLabelChange: (String) -> Unit,
    onRoutineTotalChange: (String) -> Unit,
    onRoutineStepChange: (String) -> Unit,
    onRestrictionTypeChange: (RestrictionType) -> Unit,
    onRestrictionTimeChange: (String) -> Unit,
    onTagAdd: (String) -> Unit,
    onTagRemove: (String) -> Unit
) {


    val scrollState = rememberScrollState()
    val inputMission = uiState.inputMission

    val currentCategory = when (inputMission) {
        is ExerciseMission -> MissionCategory.EXERCISE
        is DietMission -> MissionCategory.DIET
        is RoutineMission -> MissionCategory.ROUTINE
        is RestrictionMission -> MissionCategory.RESTRICTION
        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(AppTheme.dimens.large),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.doubleExtraLarge)
    ) {
        // 1. 공통 입력
        StyledCard(
            content = {
                Column(
                    modifier = Modifier.padding(AppTheme.dimens.extraLarge),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraLarge)
                ) {
                    CommonInputSection(
                        name = inputMission?.title ?: "",
                        onNameChange = onTitleChange,
                        selectedDays = inputMission?.days ?: emptySet(),
                        onDayToggle = onDayToggle
                    )
                }
            }
        )

        // 2. 카테고리 선택 & 상세 설정
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "카테고리 설정",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textSecondary
            )

            CategorySelectionTab(
                selectedCategory = currentCategory,
                onCategorySelected = onCategorySelected
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    AnimatedContent(
                        targetState = inputMission,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                        },
                        contentKey = { mission ->
                            when (mission) {
                                is ExerciseMission -> MissionCategory.EXERCISE
                                is DietMission -> MissionCategory.DIET
                                is RoutineMission -> MissionCategory.ROUTINE
                                is RestrictionMission -> MissionCategory.RESTRICTION
                                else -> null
                            }
                        },
                        label = "CategoryContent"
                    ) { mission ->
                        when (mission) {
                            is ExerciseMission -> ExerciseSettingForm(
                                selectedUnit = mission.unit,
                                onUnitSelected = onExerciseUnitChange,
                                // UI에서는 String으로 표시 (0일 경우 빈값)
                                targetValue = if (mission.targetValue == 0) "" else mission.targetValue.toString(),
                                onTargetValueChange = onExerciseTargetChange,
                                useTimer = mission.useTimer,
                                onUseTimerChange = onExerciseTimerToggle
                            )

                            is DietMission -> DietSettingForm (
                                recordMethod = mission.recordMethod,
                                onMethodSelected = onDietMethodChange
                            )

                            is RoutineMission -> DailySettingForm (
                                totalTarget = if (mission.dailyTargetAmount == 0) "" else mission.dailyTargetAmount.toString(),
                                onTotalTargetChange = onRoutineTotalChange,
                                stepAmount = if (mission.amountPerStep == 0) "" else mission.amountPerStep.toString(),
                                onStepAmountChange = onRoutineStepChange,
                                unitLabel = mission.unitLabel,
                                onUnitLabelChange = onRoutineUnitLabelChange
                            )

                            is RestrictionMission -> RestrictionSettingForm (
                                type = mission.type,
                                onTypeSelected = onRestrictionTypeChange,
                                // 분 -> 시간 변환하여 표시
                                maxAllowedTime = mission.maxAllowedMinutes?.div(60)?.toString() ?: "",
                                onMaxTimeChange = onRestrictionTimeChange
                            )

                            else -> { /* 선택 안됨 */ }
                        }
                    }
                }
            }
        }

        // 3. 태그 입력
        if (inputMission != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                var currentTagInput by remember { mutableStateOf("") }

                Column(modifier = Modifier.padding(20.dp)) {
                    TagInputSection (
                        tags = inputMission.tags,
                        inputValue = currentTagInput,
                        onValueChange = { currentTagInput = it },
                        onAddTag = {
                            onTagAdd(currentTagInput)
                            currentTagInput = "" // 입력창 비우기
                        },
                        onRemoveTag = onTagRemove
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_MissionCreationContent() {
    val dummyMission = ExerciseMission(
        id = "test",
        title = "아침 조깅",
        days = emptySet(),
        notificationTime = null,
        targetValue = 30,
        unit = ExerciseUnit.TIME,
        useTimer = true,
        tags = listOf("유산소", "오전")
    )

    MissionCreationContent(
        uiState = MissionState(inputMission = dummyMission),

        onTitleChange = {},
        onDayToggle = {},
        onCategorySelected = {},
        onExerciseUnitChange = {},
        onExerciseTargetChange = {},
        onExerciseTimerToggle = {},
        onDietMethodChange = {},
        onRoutineUnitLabelChange = {},
        onRoutineTotalChange = {},
        onRoutineStepChange = {},
        onRestrictionTypeChange = {},
        onRestrictionTimeChange = {},
        onTagAdd = {},
        onTagRemove = {}
    )
}
