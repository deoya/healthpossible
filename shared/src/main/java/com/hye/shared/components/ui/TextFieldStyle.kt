package com.hye.shared.components.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme

data class TextFieldStyle(
    val value: String,
    val onValueChange: (String) -> Unit,
    val placeholder: String? = null,
    val suffix: String? = null,
    val trailingIcon: @Composable (() -> Unit)? = null,
    val modifier:Modifier = Modifier.fillMaxWidth(),
    val shape: Dp? = null,
    val singleLine: Boolean = true,
    //val label: String? = null,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val imeAction: ImeAction = ImeAction.Default,
    val onDone: (() -> Unit)? = null,
    val focusedBorderColor : Color? = null,
    val unfocusedBorderColor : Color? = null,
    val focusedContainerColor  : Color? = null,
    val unfocusedContainerColor  : Color? = null,
    val cursorColor : Color? = null
)


@Composable
fun StyledTextField(
    style : TextFieldStyle,
) {
    val placeholder = style.placeholder ?: ""
    OutlinedTextField(
        value = style.value,
        onValueChange = style.onValueChange,
        placeholder = { Text(placeholder, color = Color.LightGray) },
        suffix = if (style.suffix != null) { { Text(style.suffix, color = AppTheme.colors.textSecondary, fontWeight = FontWeight.Bold) } } else null,
        trailingIcon = style.trailingIcon,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(style.shape ?: AppTheme.dimens.medium),
        singleLine = style.singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = style.keyboardType, imeAction = style.imeAction),
        keyboardActions = KeyboardActions(onDone = { style.onDone?.invoke() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = style.focusedBorderColor ?: AppTheme.colors.mainColor,
            unfocusedBorderColor = style.unfocusedBorderColor ?: AppTheme.colors.uncheckedTrackColor,
            focusedContainerColor = style.focusedContainerColor ?: AppTheme.colors.background,
            unfocusedContainerColor = style.unfocusedContainerColor ?: AppTheme.colors.backgroundMuted.copy(alpha = 0.5f),
            cursorColor = style.cursorColor ?: AppTheme.colors.mainColor
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
    )
}
