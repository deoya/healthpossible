package com.hye.shared.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class HPAppColors (
    // 기본 테마 색상
    val primary : Color,
    val primaryLight: Color, // 선택된 항목의 배경 등 강조가 필요한 곳에 사용
    val onPrimary: Color,
    // 배경 및 표면 색상
    val background: Color,
    val backgroundMuted: Color,
    val surface: Color,
    // 텍스트 색상
    val textPrimary: Color,
    val textSecondary: Color,
    // 기타 UI 요소 색상
    val inactive: Color,// 비활성화된 아이콘, 텍스트 등에 사용
    val divider: Color,// 구분선 색상
    val iconColor: Color,
    val mainColor: Color,
    val mainColorLight: Color,
    val successColor: Color,
    val selectedLabelColor: Color,
    val uncheckedTrackColor: Color,
    val cardBorderColor: Color
)

val LightColors = HPAppColors(
    primary = Color(0xFF5174FF),
    primaryLight = Color(0xFFE8EFFF),
    onPrimary = Color.White,
    background = Color.White,
    backgroundMuted = Color(0xFFF8FAFC),
    surface = Color.White,
    textPrimary = Color(0xFF1E293B),
    textSecondary = Color(0xFF64748B),
    inactive = Color(0xFFBDBDBD),
    divider = Color(0xFFEEEEEE),
    iconColor = Color.White,
    mainColor = Color(0xFF3B82F6),
    mainColorLight = Color(0xFFEFF6FF),
    successColor = Color(0xFF10B981),
    selectedLabelColor = Color.White,
    uncheckedTrackColor = Color(0xFFE2E8F0),
    cardBorderColor = Color(0xFFF1F5F9)
)

val LocalColors = staticCompositionLocalOf { LightColors }