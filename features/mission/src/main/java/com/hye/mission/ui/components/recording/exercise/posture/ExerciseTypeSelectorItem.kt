package com.hye.mission.ui.components.recording.exercise.posture

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.ui.common.light
import com.hye.shared.ui.common.selectionContentColor

@Composable
fun ExerciseTypeSelectorItem(
    type : AiExerciseType,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val activeColor = AppTheme.colors.mainColor
    val inactiveBg = AppTheme.colors.backgroundMuted

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimens.s))
            .clickable(onClick = onClick)
            .padding(bottom = AppTheme.dimens.md)
    ) {
        // 왼쪽 아이콘/썸네일 영역
        Box(
            modifier = Modifier
                .size(AppTheme.dimens.exciseTypeThumbnailSize)
                .clip(RoundedCornerShape(AppTheme.dimens.s))
                .background(isSelected.selectionContentColor(activeColor.light,inactiveBg))
            ,
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    Icons.Default.FitnessCenter,
                    null,
                    tint = activeColor,
                    modifier = Modifier.size(AppTheme.dimens.xxl)
                )
            } else {
                Text(
                    text = type.label.first().toString(),
                    color = AppTheme.colors.textSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppTheme.dimens.l.toSp
                )
            }
        }

        // 중앙 텍스트 정보
        Column(modifier = Modifier.weight(1f),  verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxxxs)) {
            Text(
                text = type.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) activeColor else AppTheme.colors.textPrimary
            )
            Text(
                text = type.description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary
            )
        }

        // 오른쪽 상태 아이콘
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Playing",
                tint = activeColor,
                modifier = Modifier.size(AppTheme.dimens.xxxxl)
            )
        }
    }
}