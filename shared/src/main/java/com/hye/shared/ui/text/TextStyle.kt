package com.hye.shared.ui.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp

@Composable
fun SectionTitle(text: String) = Text(
    text = text,
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold,
    color = AppTheme.colors.textPrimary,
    modifier = Modifier.padding(bottom = AppTheme.dimens.xxs)
)

@Composable
fun MenuLabel(
    text: String,
    color: Color = AppTheme.colors.textPrimary,
    weight: FontWeight = FontWeight.Bold,
    size: TextUnit = AppTheme.dimens.sm.toSp
) = Text(
    text = text,
    color = color,
    fontWeight = weight,
    fontSize = size
)
@Composable
fun TextDescription(
    text: String,
    color: Color = AppTheme.colors.textSecondary,
    modifier: Modifier = Modifier,
    lineHeight: TextUnit = TextUnit.Unspecified,
    weight: FontWeight? = null
) = Text(
    text = text,
    color = color,
    style = MaterialTheme.typography.bodySmall,
    modifier = modifier,
    lineHeight = lineHeight,
    fontWeight = weight
)

@Composable
fun TextSubheading(
    text: String,
    size: TextUnit = TextUnit.Unspecified,
    color: Color = AppTheme.colors.textSecondary,
    modifier: Modifier = Modifier,
    weight: FontWeight = FontWeight.Normal
) = Text(
    text = text,
    style = MaterialTheme.typography.bodyLarge,
    fontWeight = weight,
    color = color,
    modifier = modifier,
    fontSize = size
)

@Composable
fun TextBody(
    text: String,
    color: Color = AppTheme.colors.textPrimary,
    fontWeight: FontWeight = FontWeight.Normal,
    lineHeight: TextUnit = TextUnit.Unspecified
) = Text(
    text = text,
    style = MaterialTheme.typography.bodyMedium,
    color = color,
    fontWeight = fontWeight,
    lineHeight = lineHeight
)

@Composable
fun TitleSmall(text:String, color: Color = AppTheme.colors.textSecondary,modifier: Modifier = Modifier) = Text(
    text = text,
    style = MaterialTheme.typography.titleSmall,
    fontWeight = FontWeight.Normal,
    color = color,
    modifier = modifier
)

@Composable
fun TitleMedium(text:String, color: Color = AppTheme.colors.textPrimary,modifier: Modifier = Modifier) = Text(
    text = text,
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold,
    color = color,
    modifier = modifier
)

@Composable
fun LabelSmall(text:String, fontWeight: FontWeight = FontWeight.Normal, color: Color = AppTheme.colors.textSecondary,modifier: Modifier = Modifier) = Text(
    text = text,
    style = MaterialTheme.typography.labelSmall,
    fontWeight = fontWeight,
    color = color,
    modifier = modifier
)

@Composable
fun LabelMedium(text:String, fontWeight: FontWeight = FontWeight.Normal, color: Color = AppTheme.colors.textSecondary) = Text(
    text = text,
    style = MaterialTheme.typography.labelMedium,
    fontWeight = fontWeight,
    color = color
)

@Composable
fun DisplayTextSmall(text: String, color : Color = AppTheme.colors.background,modifier: Modifier = Modifier) = Text(
    text = text,
    style = MaterialTheme.typography.displaySmall,
    fontWeight = FontWeight.Bold,
    color = color,
    modifier = modifier,
)

@Composable
fun DisplayText(
    text: String,
    color: Color = AppTheme.colors.textSecondary,
    weight: FontWeight = FontWeight.Bold,
    size: TextUnit = AppTheme.dimens.md.toSp,
    letterSpacing: TextUnit = TextUnit.Unspecified
) = Text(
    text = text,
    style = MaterialTheme.typography.displaySmall,
    fontSize = size,
    fontWeight = weight,
    color = color,
    letterSpacing = letterSpacing
)