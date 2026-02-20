package com.hye.shared.ui.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.hye.shared.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun TrailingIcon(isChecking: Boolean, isValid: Boolean, errorMessage: String){
    if (isChecking) CircularProgressIndicator(modifier = Modifier.size(AppTheme.dimens.xxl), strokeWidth = AppTheme.dimens.xxxxxs, color = AppTheme.colors.mainColor)
    else if (isValid) Icon(Icons.Default.Check, null, tint = AppTheme.colors.mainColor)
    else if (errorMessage.isNotEmpty()) Icon(Icons.Default.Warning, null, tint = AppTheme.colors.error)
}

object StyledInputField {

    @Composable
    operator fun invoke(
        onDebouncedValueChange: ((String) -> Unit)? = null,
        debounceInterval: Long = 0L,

        value: String,
        onValueChange: (String) -> Unit = {},
        label : @Composable (() -> Unit)? = null,
        placeholder: String = "",
        suffix: String = "",
        trailingIcon: @Composable (() -> Unit) = {},
        modifier:Modifier = Modifier.fillMaxWidth(),
        shape: Dp = AppTheme.dimens.sm,

        singleLine: Boolean = true,
        isError: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text,
        imeAction: ImeAction = ImeAction.Default,

        keyboardActions: () -> Unit = {},

        focusedBorderColor : Color = AppTheme.colors.mainColor,
        unfocusedBorderColor : Color = AppTheme.colors.uncheckedTrackColor,
        focusedContainerColor : Color = AppTheme.colors.background,
        unfocusedContainerColor : Color = AppTheme.colors.backgroundMuted.copy(alpha = 0.5f),
        cursorColor : Color = AppTheme.colors.mainColor,

        readOnly: Boolean = false,
    ){

        val debounceFlow = remember { MutableStateFlow(value) }
        val currentOnDebouncedValueChange by rememberUpdatedState(onDebouncedValueChange)

        // debounceInterval이 0보다 크고 콜백이 존재할 때만 스트림 관찰
        if (debounceInterval > 0L && currentOnDebouncedValueChange != null) {
            LaunchedEffect(Unit) {
                debounceFlow
                    .debounce(debounceInterval)
                    .distinctUntilChanged()
                    .collect { debouncedValue ->
                        currentOnDebouncedValueChange?.invoke(debouncedValue)
                    }
            }
        }

        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                if (debounceInterval > 0L) {
                    debounceFlow.value = newValue
                }

            },
            label = label,
            placeholder = {if(placeholder.isEmpty()) null else Text(placeholder)},
            suffix = {if (suffix.isEmpty()) null else { Text(suffix, color = AppTheme.colors.textSecondary, fontWeight = FontWeight.Bold) }},
            trailingIcon = trailingIcon,
            modifier = modifier,
            shape = RoundedCornerShape(shape),
            singleLine = singleLine,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(onDone = { keyboardActions() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedContainerColor = focusedContainerColor,
                unfocusedContainerColor = unfocusedContainerColor,
                cursorColor = cursorColor
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            readOnly = readOnly,
        )
    }

    @Composable
    fun Section(
        onDebouncedValueChange: ((String) -> Unit)? = null,
        debounceInterval: Long = 0L,

        value: String,
        onValueChange: (String) -> Unit = {},
        label : @Composable (() -> Unit) ={},
        placeholder: String = "",
        suffix: String = "",
        trailingIcon: @Composable (() -> Unit) = {},
        modifier:Modifier = Modifier.fillMaxWidth(),
        shape: Dp = AppTheme.dimens.sm,

        singleLine: Boolean = true,
        isError: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text,
        imeAction: ImeAction = ImeAction.Default,

        keyboardActions: () -> Unit = {},

        focusedBorderColor : Color = AppTheme.colors.mainColor,
        unfocusedBorderColor : Color = AppTheme.colors.uncheckedTrackColor,
        focusedContainerColor : Color = AppTheme.colors.background,
        unfocusedContainerColor : Color = AppTheme.colors.backgroundMuted.copy(alpha = 0.5f),
        cursorColor : Color = AppTheme.colors.mainColor,

        readOnly: Boolean = false,

        space : Dp = AppTheme.dimens.xxs,
        description : @Composable () -> Unit = {}
    ){
        Column(verticalArrangement = Arrangement.spacedBy(space)) {
            label()
            invoke(
                onDebouncedValueChange = onDebouncedValueChange,
                debounceInterval = debounceInterval,
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder,
                suffix = suffix,
                trailingIcon = trailingIcon,
                modifier = modifier,
                shape = shape,
                singleLine = singleLine,
                isError = isError,
                keyboardType = keyboardType,
                imeAction = imeAction,
                keyboardActions = keyboardActions,
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedContainerColor = focusedContainerColor,
                unfocusedContainerColor = unfocusedContainerColor,
                cursorColor = cursorColor,
                readOnly = readOnly
            )
            description()
        }
    }
}


