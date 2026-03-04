package com.hye.mission.ui.components.mission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.hye.features.mission.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.util.text


@Composable
fun StartButton(
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    completedText: String = R.string.mission_today_completed.text,
    defaultText: String = R.string.mission_record_start.text
) {
    Button(
        onClick = {
            // 완료되지 않았을 때만 클릭 이벤트 실행
            if (!isCompleted) {
                onClick()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.iconBox),
        shape = RoundedCornerShape(AppTheme.dimens.md),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isCompleted) AppTheme.colors.backgroundMuted else AppTheme.colors.mainColor,
            contentColor = if (isCompleted) AppTheme.colors.textSecondary else Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isCompleted) AppTheme.dimens.zero else AppTheme.dimens.xxxxs,
            pressedElevation = if (isCompleted) AppTheme.dimens.zero else AppTheme.dimens.xxs
        )
    ) {
        if (isCompleted) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(AppTheme.dimens.l)
                )
                Text(
                    text = completedText,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppTheme.dimens.md.toSp
                )
            }
        } else {
            Text(
                text = defaultText,
                fontWeight = FontWeight.Bold,
                fontSize = AppTheme.dimens.md.toSp
            )
        }
    }
}