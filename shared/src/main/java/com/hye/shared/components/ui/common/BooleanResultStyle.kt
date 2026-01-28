package com.hye.shared.components.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hye.shared.theme.AppTheme


//선택 관련
val Boolean.selectionBorderColor: Color
    @ReadOnlyComposable
    @Composable
    get() = if (this) AppTheme.colors.mainColor else AppTheme.colors.unSelectedColor

@ReadOnlyComposable
@Composable
fun Boolean.selectionBorderStroke(width: Dp = AppTheme.dimens.one, color:Color = AppTheme.colors.unSelectedColor): BorderStroke? =
    if (this) null else BorderStroke(width, color)

val Boolean.selectionIconMenuColor: Color
    @ReadOnlyComposable
    @Composable
    get() = if (this) AppTheme.colors.mainColor else AppTheme.colors.textSecondary

val Boolean.selectionFontWeight: FontWeight
    @ReadOnlyComposable
    @Composable
    get() = if (this)FontWeight.Bold else FontWeight.Medium

@ReadOnlyComposable
@Composable
fun Boolean.selectionBtnColor(selection :Color = AppTheme.colors.mainColor, deselection : Color = AppTheme.colors.background): Color =
    if (this) selection else deselection

@ReadOnlyComposable
@Composable
fun Boolean.selectionBtnContentColor(selection :Color = AppTheme.colors.background, deselection : Color = AppTheme.colors.textSecondary): Color =
    if (this) selection else deselection

val Boolean.selectionBtnColorLight: Color
    @ReadOnlyComposable
    @Composable
    get() = if (this) AppTheme.colors.mainColorLight else AppTheme.colors.backgroundMuted

// 완료 관련
@ReadOnlyComposable
@Composable
fun Boolean.completedColor(completed : Color = AppTheme.colors.completeColor, incomplete:Color = AppTheme.colors.mainColor): Color =
    if (this) incomplete else completed

@ReadOnlyComposable
@Composable
fun Boolean.completedIcon(incomplete: ImageVector = Icons.Default.Work, completed: ImageVector = Icons.Default.Check): ImageVector =
    if (this) completed else incomplete

@ReadOnlyComposable
@Composable
fun Boolean.completedElevation(completed : Dp = AppTheme.dimens.zero, incomplete:Dp = AppTheme.dimens.xxxxxs):Dp =
    if (this) completed else incomplete

@ReadOnlyComposable
@Composable
fun Boolean.completedBorder(
    completed: BorderStroke = BorderStroke(
        AppTheme.dimens.zero,
        Color.Unspecified
    ),
    incomplete: BorderStroke = BorderStroke(AppTheme.dimens.zero, AppTheme.colors.incompleteColor)
): BorderStroke = if (this) completed else incomplete

//비 활성 화 관련
@ReadOnlyComposable
@Composable
fun Boolean.enabledColor(enabled : Color = AppTheme.colors.mainColor, disabled:Color = AppTheme.colors.textSecondary.copy(
    AppTheme.dimens.alphaMuted)): Color =
    if (this) enabled else disabled