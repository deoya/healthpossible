package com.hye.shared.ui.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp

enum class TextStyleSize {
    Small,
    Medium,
    Large,
}

@Composable
fun LabelText(
    text: String,
    style: TextStyleSize = TextStyleSize.Medium,
    fontWeight: FontWeight = FontWeight.Bold,
    color: Color = AppTheme.colors.textSecondary,
    lineHeight: TextUnit = TextUnit.Unspecified,
) {
    val resultStyle = when (style) {
        TextStyleSize.Small -> {
            MaterialTheme.typography.labelSmall
        }

        TextStyleSize.Medium -> {
            MaterialTheme.typography.labelMedium
        }

        TextStyleSize.Large -> {
            MaterialTheme.typography.labelLarge
        }
    }
    Text(
        text = text,
        style = resultStyle,
        fontWeight = fontWeight,
        color = color,
        lineHeight = lineHeight
    )
}
//Todo : style별이 아니라 typograpy 별.  상황별로 묶기

//------------------------------------------
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
fun ErrorMessage(
    errorMessage: String,
    errorColor: Color = AppTheme.colors.error,
    modifier: Modifier = Modifier.padding(
        top = AppTheme.dimens.xxxxs,
        start = AppTheme.dimens.xxs
    )
) = Text(
    text = errorMessage,
    color = errorColor,
    style = MaterialTheme.typography.labelSmall,
    modifier = modifier
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
    weight: FontWeight = FontWeight.Normal,
    lineHeight: TextUnit = TextUnit.Unspecified,
) = Text(
    text = text,
    style = MaterialTheme.typography.bodyLarge,
    fontWeight = weight,
    color = color,
    modifier = modifier,
    fontSize = size,
    lineHeight = lineHeight,
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
fun TitleSmall(
    text: String,
    color: Color = AppTheme.colors.textSecondary,
    modifier: Modifier = Modifier
) = Text(
    text = text,
    style = MaterialTheme.typography.titleSmall,
    fontWeight = FontWeight.Normal,
    color = color,
    modifier = modifier
)

@Composable
fun TitleMedium(
    text: String,
    color: Color = AppTheme.colors.textPrimary,
    modifier: Modifier = Modifier,
    size: TextUnit = TextUnit.Unspecified
) = Text(
    text = text,
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold,
    color = color,
    modifier = modifier,
    fontSize = size
)

@Composable
fun LabelSmall(
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = AppTheme.colors.textSecondary,
    modifier: Modifier = Modifier
) = Text(
    text = text,
    style = MaterialTheme.typography.labelSmall,
    fontWeight = fontWeight,
    color = color,
    modifier = modifier
)

@Composable
fun LabelMedium(
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = AppTheme.colors.textSecondary
) = Text(
    text = text,
    style = MaterialTheme.typography.labelMedium,
    fontWeight = fontWeight,
    color = color
)

@Composable
fun DisplayTextSmall(
    text: String,
    color: Color = AppTheme.colors.textPrimary,
    modifier: Modifier = Modifier
) = Text(
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