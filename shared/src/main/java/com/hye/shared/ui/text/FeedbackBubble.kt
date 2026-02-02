package com.hye.shared.ui.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.hye.shared.ui.common.light
import com.hye.shared.theme.AppTheme

@Composable
fun FeedbackBubble(message: String) {
    Surface(
        color = AppTheme.colors.supportAgentColor.light,
        shape = RoundedCornerShape(AppTheme.dimens.md)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = AppTheme.dimens.md, vertical = AppTheme.dimens.s),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)
        ) {
            Icon(Icons.Default.AutoAwesome, null, tint = AppTheme.colors.background, modifier = Modifier.size(AppTheme.dimens.l))
            LabelSmall(message, color = AppTheme.colors.background, fontWeight = FontWeight.Bold)
        }
    }
}