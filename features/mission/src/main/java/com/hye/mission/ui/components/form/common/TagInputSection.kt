package com.hye.mission.ui.components.form.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hye.features.mission.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.chip.StyledTag
import com.hye.shared.ui.chip.Tag
import com.hye.shared.ui.icon.CloseIcon
import com.hye.shared.ui.text.TitleSmall
import com.hye.shared.ui.text.StyledInputField
import com.hye.shared.util.text
import com.hye.shared.R as CommonR


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagInputSection(
    tags: List<String>,
    inputValue: String,
    onValueChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)) {
        TitleSmall(R.string.mission_tags.text)

        StyledInputField(
            value = inputValue,
            onValueChange = onValueChange,
            placeholder = R.string.mission_tags_placeholder.text,
            trailingIcon = {
                IconButton(onClick = onAddTag) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = CommonR.string.add.text,
                        tint = AppTheme.colors.mainColor
                    )
                }
            },
            keyboardActions = { onAddTag() }
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxs)
        ) {
            tags.forEach { tag ->
                StyledTag(
                    onClick = {onRemoveTag(tag)},
                    content= { Tag(tag) },
                    content2 = { CloseIcon() },
                    border = BorderStroke(
                    AppTheme.dimens.one,
                    AppTheme.colors.mainColor.copy(alpha = AppTheme.dimens.alphaMuted)
                ),)
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TagInputSectionPreview() {
        var tags by remember { mutableStateOf(listOf("운동", "영어 공부", "안드로이드")) }
        var inputValue by remember { mutableStateOf("") }

        Box(modifier = Modifier.padding(16.dp)) {
            TagInputSection(
                tags = tags,
                inputValue = inputValue,
                onValueChange = { inputValue = it },
                onAddTag = {
                    if (inputValue.isNotBlank() && !tags.contains(inputValue)) {
                        tags = tags + inputValue.trim()
                        inputValue = ""
                    }
                },
                onRemoveTag = { tagToRemove ->
                    tags = tags - tagToRemove
                }
            )
        }
}