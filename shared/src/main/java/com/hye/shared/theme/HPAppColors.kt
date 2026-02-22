package com.hye.shared.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

data class HPAppColors (
    // 기본 테마 색상
    val primary : Color,
    val primaryLight: Color, // 선택된 항목의 배경 등 강조가 필요한 곳에 사용
    val onPrimary: Color,
    val error : Color,
    // 배경 및 표면 색상
    val background: Color,
    val backgroundMuted: Color,
    val surface: Color,
    // 텍스트 색상
    val textPrimary: Color,
    val textSecondary: Color,
    //카테고리별 색상
    val exerciseColor: Color,
    val exerciseSecondColor: Color,
    val dietColor: Color,
    val dietSecondColor: Color,
    val routineColor: Color,
    val routineSecondColor: Color,
    val restrictionColor: Color,
    val restrictionSecondColor: Color,
    val supportAgentColor : Color,
    val supportAgentSecondColor : Color,
    // 기타 UI 요소 색상
    val inactive: Color,// 비활성화된 아이콘, 텍스트 등에 사용
    val divider: Color,// 구분선 색상
    val dark: Color,
    val darkScreen: Color,
    val mainColor: Color,
    val mainColorLight: Color,
    val successColor: Color,
    val selectedLabelColor: Color,
    val uncheckedTrackColor: Color,
    val cardBorderColor: Color,
    val chipBorderColor: Color,
    val chipSelectedColor: Color,
    val completeColor : Color,
    val incompleteColor:Color,
    val inputBoxBorder: Color,

    val unSelectedColor : Color,

    val mapSimulationColor : Color,

    val InheritColor : Color,
)

val LightColors = HPAppColors(
    primary = Color(0xFF5174FF),
    primaryLight = Color(0xFFE8EFFF),
    onPrimary = Color.White,
    error = Color(0xFFFF7A6E),
    background = Color.White,
    backgroundMuted = Color(0xFFF8FAFC),
    surface = Color.White,
    textPrimary = Color(0xFF1E293B),
    textSecondary = Color(0xFF64748B),

    exerciseColor=Color(0xFF1168EC),
    exerciseSecondColor=Color(0xFF8FBEF2),
    dietColor=Color(0xFFF3B128),
    dietSecondColor=Color(0xFFF6DFB4),
    routineColor=Color(0xFFEB5DCC),
    routineSecondColor=Color(0xFFF4B0EB),
    restrictionColor=Color(0xFF04B09E),
    restrictionSecondColor=Color(0xFFb9f29a),
    supportAgentColor = Color(0xFF905EEF),
    supportAgentSecondColor = Color(0xFFCDB6FC),


    inactive = Color(0xFFE7E7E7),
    divider = Color(0xFFEEEEEE),
    dark = Color.Black,
    darkScreen = Color.DarkGray,
    mainColor = Color(0xFF3B82F6),
    mainColorLight = Color(0xFFEFF6FF),
    successColor = Color(0xFF10B981),
    selectedLabelColor = Color.White,
    uncheckedTrackColor = Color(0xFFE2E8F0),
    cardBorderColor = Color(0xFFF1F5F9),
    chipBorderColor = Color.Transparent,
    chipSelectedColor = Color.Transparent,
    completeColor = Color.Gray,
    incompleteColor =  Color.Transparent,
    unSelectedColor = Color.Transparent,
    inputBoxBorder = Color.LightGray,
    mapSimulationColor = Color(0xFFE5E7EB),
    InheritColor = Color.Transparent
)

val Color.alphaPrimary
    @Composable
    @ReadOnlyComposable
    get() =  with(LocalDensity.current) { this@alphaPrimary.copy(alpha = AppTheme.dimens.alphaPrimary) }


val LocalColors = staticCompositionLocalOf { LightColors }