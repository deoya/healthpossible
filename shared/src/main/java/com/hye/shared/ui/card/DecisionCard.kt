package com.hye.shared.ui.card
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hye.shared.theme.AppTheme


@Composable
fun DecisionCard(
    leadingIcon: @Composable () -> Unit,
    trailingBadge: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White
) {
    StyledCard(
        CardStyle(
            modifier = modifier,
            shape = RoundedCornerShape(AppTheme.dimens.xxl),
            containerColor = containerColor,
            elevation = AppTheme.dimens.xxs
        )
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.dimens.l),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.l),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon()
                trailingBadge()
            }

            title()
            description()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)
            ) {
                actions()
            }
        }
    }
}