package com.hye.shared.components.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.hye.shared.theme.AppTheme

/*ScreenTitle()
SectionTitle()     // 소제목
SectionSubtitle()
BodyText()
CaptionText()
HelperText()*/

@Composable
fun SectionTitle(text:String) = Text(
    text = text,
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold,
    color = AppTheme.colors.textPrimary,
    modifier = Modifier.padding(bottom = AppTheme.dimens.extraSmall)
)

@Composable
fun TitleMedium(text:String) = Text(
    text = text,
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold,
    color = AppTheme.colors.textPrimary
)

@Composable
fun LabelSmall(text:String, fontWeight: FontWeight = FontWeight.Bold, color: Color = AppTheme.colors.textSecondary) = Text(
    text = text,
    style = MaterialTheme.typography.labelSmall,
    fontWeight = fontWeight,
    color = color
)

@Composable
fun LabelMedium(text:String, fontWeight: FontWeight = FontWeight.Bold, color: Color = AppTheme.colors.textSecondary) = Text(
    text = text,
    style = MaterialTheme.typography.labelMedium,
    fontWeight = fontWeight,
    color = color
)

@Composable
fun BodyTextSmall(text:String, color: Color = AppTheme.colors.mainColor, lineHeight: TextUnit = TextUnit.Unspecified) = Text(
    text = text,
    style = MaterialTheme.typography.bodySmall,
    color = color,
    lineHeight = lineHeight
)

@Composable
fun BodyTextMedium(text: String, color: Color = AppTheme.colors.textPrimary, fontWeight: FontWeight = FontWeight.Bold,lineHeight: TextUnit = TextUnit.Unspecified) = Text(
    text = text,
    style = MaterialTheme.typography.bodySmall,
    color = color,
    fontWeight = fontWeight,
    lineHeight = lineHeight
)