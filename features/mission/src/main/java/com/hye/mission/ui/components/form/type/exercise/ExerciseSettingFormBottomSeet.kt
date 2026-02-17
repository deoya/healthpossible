package com.hye.mission.ui.components.form.type.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.mission.ui.components.recording.exercise.posture.ExerciseTypeSelectorItem
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.sheet.CommonBottomSheet

@Composable
fun ExerciseSettingFormBottomSeet(
    onDismissRequest : (Boolean)-> Unit,
    selectedExerciseType : String,
    itemOnclick : (AiExerciseType) -> Unit
) {
    CommonBottomSheet (
        sheetHeightFraction = AppTheme.dimens.sheetHeight,
        onDismissRequest = {onDismissRequest(false)},
        header = {}
    ) {
        LazyColumn (
            contentPadding = PaddingValues(
                horizontal = AppTheme.dimens.xxl,
                vertical = AppTheme.dimens.xxs
            ),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.md)
        ) {
            itemsIndexed(AiExerciseType.entries) { _, exercise ->
                ExerciseTypeSelectorItem (
                    type = exercise,
                    isSelected = selectedExerciseType == exercise.label,
                    onClick = { itemOnclick(exercise) }
                )
            }
        }
    }
}