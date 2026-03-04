package com.hye.shared.ui.bubble

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.ui.text.TypewriterText

@Composable
fun MessageBubble(text: String) {
    Surface(
        color = Color.Black.copy(alpha = 0.3f),
        shape = RoundedCornerShape(AppTheme.dimens.md),
        border = BorderStroke(AppTheme.dimens.one, Color.White.copy(alpha = 0.2f)),
        modifier = Modifier.padding(horizontal = AppTheme.dimens.xxxxl)
    ) {
        TypewriterText(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                lineHeight = AppTheme.dimens.xxl.toSp,
                textAlign = TextAlign.Center
            ),
            color = Color.White,
            modifier = Modifier.padding(AppTheme.dimens.l)
        )
    }
}
