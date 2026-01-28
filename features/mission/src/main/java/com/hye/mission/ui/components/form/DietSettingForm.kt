package com.hye.mission.ui.components.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hye.domain.model.mission.types.DietRecordMethod
import com.hye.features.mission.R
import com.hye.mission.ui.util.getAppearance
import com.hye.shared.components.ui.common.selectionBorderColor
import com.hye.shared.components.ui.common.selectionBorderStroke
import com.hye.shared.components.ui.common.selectionBtnColorLight
import com.hye.shared.components.ui.common.selectionFontWeight
import com.hye.shared.components.ui.common.selectionIconMenuColor
import com.hye.shared.components.ui.CardStyle
import com.hye.shared.components.ui.LabelMedium
import com.hye.shared.components.ui.MenuStyle
import com.hye.shared.components.ui.StyledAlert
import com.hye.shared.components.ui.StyledCard
import com.hye.shared.components.ui.StyledMenu
import com.hye.shared.components.ui.TextDescription
import com.hye.shared.components.ui.common.IconStyle
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.util.text


@Composable
fun DietSettingForm(
    recordMethod: DietRecordMethod,
    onMethodSelected: (DietRecordMethod) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.md)) {
        LabelMedium(R.string.mission_plan_the_way_diet_record.text)
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)) {
            DietRecordMethod.values().forEach { method ->
                val isSelected = recordMethod == method
                StyledCard(
                    CardStyle(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onMethodSelected(method) },
                        containerColor = isSelected.selectionBtnColorLight,
                        border = isSelected.selectionBorderStroke(color = isSelected.selectionBorderColor),
                    ),
                ) {
                    StyledMenu(
                        MenuStyle(
                            icon = IconStyle(method.getAppearance.icon, tint = isSelected.selectionIconMenuColor),
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
                TextDescription(
                    text = recordMethod.getAppearance.alert,
                    lineHeight = AppTheme.dimens.m.toSp,
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