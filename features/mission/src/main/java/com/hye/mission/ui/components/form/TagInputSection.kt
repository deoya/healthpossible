package com.hye.mission.ui.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hye.shared.components.ui.StyledTextField
import com.hye.shared.components.ui.TextFieldStyle
import com.hye.shared.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagInputSection(
    tags: List<String>,
    inputValue: String,
    onValueChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("태그 (선택)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AppTheme.colors.textSecondary)

        StyledTextField(TextFieldStyle(
            value = inputValue,
            onValueChange = onValueChange,
            placeholder = "태그 추가...",
            trailingIcon = {
                IconButton(onClick = onAddTag) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = "추가",
                        tint = AppTheme.colors.mainColor
                    )
                }
            },
            onDone = { onAddTag() }
        ))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AppTheme.colors.mainColorLight,
                    border = BorderStroke(1.dp, AppTheme.colors.mainColor.copy(alpha = 0.2f)),
                    modifier = Modifier.clickable { onRemoveTag(tag) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(tag, color = AppTheme.colors.mainColor, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Outlined.Close, null, modifier = Modifier.size(14.dp), tint = AppTheme.colors.mainColor)
                    }
                }
            }
        }
    }
}