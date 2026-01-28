package com.hye.mission.ui.components.mission

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.hye.features.mission.R
import com.hye.shared.components.ui.MenuLabel
import com.hye.shared.components.ui.StyledButton
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.text

@Composable
fun StartButton(
    isInProgress: Boolean,
    onClick: () -> Unit
) {
    if (isInProgress) {
        StyledButton (
            onClick = onClick,
            elevation = AppTheme.dimens.xxxxxs,
            contentPadding = PaddingValues(horizontal = AppTheme.dimens.md, vertical = AppTheme.dimens.xxs),
            modifier = Modifier.height(AppTheme.dimens.xxxxl)
        ) {
            MenuLabel(R.string.mission_recording.text, color = Color.Unspecified)
        }
    } else {
        StyledButton(
            onClick = onClick,
            shape = AppTheme.dimens.iconBox,
            containerColor = AppTheme.colors.mainColorLight,
            contentColor = Color.Unspecified,
            modifier = Modifier.size(AppTheme.dimens.xxxxl),
            contentPadding = PaddingValues(AppTheme.dimens.zero),
        ){
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = AppTheme.colors.mainColor,
                modifier = Modifier.size(AppTheme.dimens.xxl)
            )
        }
    }
}


@Preview
@Composable
fun StartButton_Preview(){
    StartButton(
        isInProgress = false,
        onClick = {})
}