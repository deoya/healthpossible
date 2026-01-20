package com.hye.mission.ui.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hye.domain.model.mission.DietRecordMethod
import com.hye.mission.ui.util.getAppearance
import com.hye.mission.ui.util.selectionBorderColor
import com.hye.mission.ui.util.selectionFontWeight
import com.hye.mission.ui.util.selectionIconMenuColor
import com.hye.shared.components.ui.BodyTextSmall
import com.hye.shared.components.ui.LabelMedium
import com.hye.shared.components.ui.MenuStyle
import com.hye.shared.components.ui.StyledAlert
import com.hye.shared.components.ui.StyledIconMenu
import com.hye.shared.theme.AppTheme


@Composable
fun DietSettingForm(
    recordMethod: DietRecordMethod,
    onMethodSelected: (DietRecordMethod) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.large)) {
        LabelMedium("기록 방식 선택")
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.mediumSmall)) {

            DietRecordMethod.values().forEach { method ->
                val isSelected = recordMethod == method

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onMethodSelected(method) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) AppTheme.colors.mainColorLight else AppTheme.colors.backgroundMuted
                    ),
                    border = BorderStroke(1.dp, isSelected.selectionBorderColor),
                    shape = RoundedCornerShape(AppTheme.dimens.large),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    StyledIconMenu(
                        MenuStyle(
                            icon = method.getAppearance.icon,
                            iconTint = isSelected.selectionIconMenuColor
                        ),
                        {
                            LabelMedium(
                                method.getAppearance.text,
                                fontWeight = isSelected.selectionFontWeight,
                                color = isSelected.selectionIconMenuColor
                            )
                        }
                    )
                }
            }
        }
        StyledAlert(
            {
                BodyTextSmall(
                    recordMethod.getAppearance.alert,
                    color = AppTheme.colors.textSecondary,
                    lineHeight = 18.sp,
                )
            }
        )
    }
}

@Preview
@Composable
fun Preview_DietSettingForm() {
    DietSettingForm(
        recordMethod = DietRecordMethod.CHECK,
        onMethodSelected = {}
    )
}